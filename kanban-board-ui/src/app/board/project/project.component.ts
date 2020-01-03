import { Component, OnInit, EventEmitter } from '@angular/core';
import { ProjectService } from '../service/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from './project';
import { User } from '../user/user';
import { Team } from '../team/team';
import { Stage } from '../stage/stage';
import { EmitAction } from '../util/emit-action';
import { Task } from '../task/task';
import { Modal } from '../util/modal';
import { EditProject } from './edit-project';
import { UtilService } from 'src/app/util/util.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  private projectId:string;
  public projectData:Project;
  public projectOwner:User;
  public projectTeams:Array<Team> = new Array<Team>();
  public projectStages:Array<Stage> = new Array<Stage>();
  public projectTotalTaskCount:number = 0;
  public showProject:boolean = false;
  public modal:Modal = new Modal();
  private gotSubscriptionData$:EventEmitter<EmitAction> = new EventEmitter<EmitAction>();
  private gotUserData:boolean = false;
  private gotTeamData:boolean = false;
  private gotStageData:boolean = false;
  private gotTaskData:boolean = false;
  isAddStage:boolean = false;
  isAddingStage:boolean = false;
  isRemovingStage:boolean = false;
  isRenameStage:boolean = false;
  isRenamingStage:boolean = false;
  currStage:Stage = new Stage();
  newStage:string = "";
  isAddTeam:boolean = false;
  isAddingTeam:boolean = false;
  isRemovingTeam:boolean = false;
  isRenameTeam:boolean = false;
  isRenamingTeam:boolean = false;
  currTeam:Team = new Team();
  newTeam:string = "";
  edit:EditProject = new EditProject();
  searchUsers:Array<User> = new Array<User>();

  constructor(private projectService:ProjectService, 
    private route: ActivatedRoute, 
    private router:Router,
    private utilService:UtilService) { }

  ngOnInit() {
    this.checkSubscriptionData();
    this.route.paramMap.subscribe(params => {
      console.log(params.get('id'));
      this.projectId = params.get("id");
      if(this.projectId) {
        this.loadProjectDetails(this.projectId);
      } else {
        this.navigateToProjectList();
      }
    });
    // Show Project Drop down in header
    this.utilService.showProjectDropDown();
    this.utilService.currPage = "project";
    this.utilService.listenToCurrentProjectChanged()
    .subscribe(
      (projectId) => {
        this.navigateToProjectDetails(projectId);
      }
    );
  }

  checkSubscriptionData() {
    this.gotSubscriptionData$.subscribe((ea:EmitAction) => {
      switch(ea.action){
        case "loadProject":
          console.log("loadProject Case");
          if(this.gotUserData && this.gotTeamData && this.gotStageData && this.gotTaskData) {
            this.showProject = true;
          }
      }
    });
  }

  loadProjectDetails(projectId:string) {
    this.projectService.fetchProjectById(projectId)
    .subscribe((project:Project) => {
      console.log("project", project);
      this.projectData = project;
      this.utilService.currProject = project.id;
      this.gotSubscriptionData$.emit(new EmitAction("loadProject", true));
      this.projectService.fetchUserById(project.ownerId).subscribe(
        (user) => {
          console.log(user);
          this.projectOwner = user;
          this.gotUserData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadProject", true));
      });
      this.projectService.fetchTeamsByProjectIds(new Array<string>(projectId))
        .subscribe((teams:Array<Team>) => {
          console.log(teams);
          this.projectTeams = teams;
          this.projectTeams.forEach((team:Team) => {
            team["taskCount"] = "-NA-";
            this.projectService.fetchTasksCountByTeamId(team.id)
            .subscribe((c:number) => {
              team["taskCount"] = c;
            });
          });
          this.gotTeamData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadProject", true));
        });
        this.projectService.fetchStagesByProjectIds(new Array<string>(projectId))
        .subscribe((stages:Array<Stage>) => {
          console.log(stages);
          this.projectStages = stages;
          this.projectStages.forEach((stage:Stage) => {
            stage["taskCount"] = "-NA-";
            this.projectService.fetchTasksCountByStageId(stage.id)
            .subscribe((c:number) => {
              stage["taskCount"] = c;
            });
          });
          this.gotStageData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadProject", true));
        });
        this.projectService.fetchTasksCountByProjectId(projectId)
        .subscribe((taskCount:number) => {
          console.log("totalTaskCount", taskCount);
          this.projectTotalTaskCount = taskCount;
          this.gotTaskData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadProject", true));
        });
    }, (err) => {
      console.log(err);
      this.navigateToProjectList();
    });
  }

  startEdit(prop:string) {
    let newProp:string = this.getNewProp(prop);
    let isProp:string = this.getIsProp(prop);
    this.projectData[newProp] = this.projectData[prop];
    this.edit[isProp] = true;
    this.edit.currProp = prop;
  }

  cancelEdit(prop:string) {
    let isProp:string = this.getIsProp(prop);
    this.edit[isProp] = false;
    this.edit.currProp = undefined;
  }

  saveEdit(prop:string) {
    let isProp:string = this.getIsProp(prop);
    let newProp:string = this.getNewProp(prop);
    this.edit[isProp] = false;
    this.edit.isSaving = true;
    console.log("new " + prop, this.projectData[newProp]);
    let updatedProject = new Project(this.projectData);
    updatedProject[prop] = this.projectData[newProp];
    console.log("updatedProject", updatedProject);
    this.projectService.updateProject(this.projectId, updatedProject)
    .subscribe((project:Project) => {
      this.projectData = project;
      console.log("edit", this.edit);
      if(prop == 'ownerId') {
        this.projectService.fetchUserById(this.projectData.ownerId).subscribe(
          (user) => {
            console.log(user);
            this.projectOwner = user;
            setTimeout(() => {
              this.edit.isSaving = false;
              this.edit.currProp = undefined;
            }, 5000);
        });
      } else {
        setTimeout(() => {
          this.edit.isSaving = false;
          this.edit.currProp = undefined;
        }, 5000);
      }
    }, (err) => {
      this.edit.isSaving = false;
      this.edit.currProp = undefined;
      this.modal.body = "Unable to edit the " + prop + "!! Please try again..";
      this.showModal(this.modal);
    });
  }

  searchUser(userId:string) {
    if(userId && userId.trim().length > 0) {
      this.projectService.fetchUserByUserIdOrNameLike(userId.trim())
      .subscribe((users:Array<User>) => {
        this.searchUsers = users;
      });
    }
  }

  setOwner(user:User) {
    this.projectData["newOwnerId"] = user.id;
    this.saveEdit('ownerId');
  }

  startAddStage() {
    this.isAddStage = true;
    //this.projectStages.push(new Stage());
  }

  cancelAddStage() {
    this.isAddStage = false;
    //this.projectStages.pop();
  }

  saveStage(name:string) {
    if(name && name.trim().length > 0) {
      this.isAddingStage = true;
      //let stage:Stage = this.projectStages[this.projectStages.length - 1];
      let stage:Stage = new Stage();
      stage.name = name.trim();
      stage.projectId = this.projectId;
      this.projectService.addStageToProject(stage)
      .subscribe((stage:Stage) => {
        console.log("new stage", stage);
        setTimeout(() => {
          this.projectStages.push(stage);
          this.isAddingStage = false;
          this.isAddStage = false;
        }, 5000);
      }, (err) => {
          this.isAddingStage = false;
          this.isAddStage = false;
          this.modal.body = "Unable to add new Stage!! Please try again..";
          this.showModal(this.modal);
      });
    } else {
      // show err msg
      this.newStage = "";
      this.modal.body = "Please enter a valid non-empty name for the new Stage!!";
      this.showModal(this.modal);
    }
  }

  removeStage(stage:Stage, index:number) {
    console.log("index", index);
    this.isRemovingStage = true;
    this.currStage = stage;
    this.projectService.removeStage(stage.id)
    .subscribe(
      () => {
        setTimeout(() => {
          this.projectStages.splice(index, 1);
          console.log("removed stage", this.projectStages);
          this.currStage = new Stage();
          this.isRemovingStage = false;
        }, 5000);
    }, (err) => {
      this.isRemovingStage = false;
      this.currStage = new Stage();
      this.modal.body = "Unable to remove the stage '" + stage.name + "', Please try again!!!";
      this.showModal(this.modal);
    });
  }

  startRenameStage(stage:Stage, index:number) {
    this.currStage = stage;
    this.currStage["index"] = index;
    this.newStage = stage.name;
    this.isRenameStage = true;
    console.log("start rename curr stage", this.currStage);
  }

  cancelRenameStage() {
    this.currStage = new Stage();
    this.isRenameStage = false;
  }

  saveRenameStage(name:string) {
    if(name && name.trim().length > 0) {
      this.isRenamingStage = true;
      let rStage:Stage = new Stage();
      rStage.id = this.currStage.id;
      rStage.name = name.trim();
      rStage.projectId = this.projectId;
      this.projectService.renameStage(rStage)
      .subscribe((stage:Stage) => {
        console.log("rename stage", stage);
        setTimeout(() => {
          this.projectStages.splice(this.currStage["index"], 1, stage);
          this.isRenamingStage = false;
          this.isRenameStage = false;
          this.newStage = "";
        }, 5000);
      }, (err) => {
          this.isRenamingStage = false;
          this.isRenameStage = false;
          this.newStage = "";
          this.modal.body = "Unable to rename Stage!! Please try again..";
          this.showModal(this.modal);
      });
    } else {
      this.newStage = "";
      this.modal.body = "Please enter a valid non-empty name for the new Stage!!";
      this.showModal(this.modal);
    }
  }

  // Team modification
  startAddTeam() {
    this.isAddTeam = true;
    //this.projectTeams.push(new Team());
  }

  cancelAddTeam() {
    this.isAddTeam = false;
    //this.projectTeams.pop();
  }

  saveTeam(name:string) {
    if(name && name.trim().length > 0) {
      this.isAddingTeam = true;
      //let team:Team = this.projectTeams[this.projectTeams.length - 1];
      let team:Team = new Team();
      team.name = name.trim();
      team.projectId = this.projectId;
      this.projectService.addTeamToProject(team)
      .subscribe((team:Team) => {
        console.log("new team", team);
        setTimeout(() => {
          this.projectTeams.push(team);
          this.isAddingTeam = false;
          this.isAddTeam = false;
        }, 5000);
      }, (err) => {
          this.isAddingTeam = false;
          this.isAddTeam = false;
          this.modal.body = "Unable to add new Team!! Please try again..";
          this.showModal(this.modal);
      });
    } else {
      // show err msg
      this.newTeam = "";
      this.modal.body = "Please enter a valid non-empty name for the new Team!!";
      this.showModal(this.modal);
    }
  }

  removeTeam(team:Team, index:number) {
    console.log("index", index);
    this.isRemovingTeam = true;
    this.currTeam = team;
    this.projectService.removeTeam(team.id)
    .subscribe(
      () => {
        setTimeout(() => {
          this.projectTeams.splice(index, 1);
          console.log("removed team", this.projectTeams);
          this.currTeam = new Team();
          this.isRemovingTeam = false;
        }, 5000);
    }, (err) => {
      this.isRemovingTeam = false;
      this.currTeam = new Team();
      this.modal.body = "Unable to remove the team '" + team.name + "', Please try again!!!";
      this.showModal(this.modal);
    });
  }

  startRenameTeam(team:Team, index:number) {
    this.currTeam = team;
    this.currTeam["index"] = index;
    this.newTeam = team.name;
    this.isRenameTeam = true;
    console.log("start rename curr team", this.currTeam);
  }

  cancelRenameTeam() {
    this.currTeam = new Team();
    this.isRenameTeam = false;
  }

  saveRenameTeam(name:string) {
    if(name && name.trim().length > 0) {
      this.isRenamingTeam = true;
      let rTeam:Team = new Team();
      rTeam.id = this.currTeam.id;
      rTeam.name = name.trim();
      rTeam.projectId = this.projectId;
      this.projectService.renameTeam(rTeam)
      .subscribe((team:Team) => {
        console.log("rename team", team);
        setTimeout(() => {
          this.projectTeams.splice(this.currTeam["index"], 1, team);
          this.isRenamingTeam = false;
          this.isRenameTeam = false;
          this.newTeam = "";
        }, 5000);
      }, (err) => {
          this.isRenamingTeam = false;
          this.isRenameTeam = false;
          this.newTeam = "";
          this.modal.body = "Unable to rename Team!! Please try again..";
          this.showModal(this.modal);
      });
    } else {
      this.newTeam = "";
      this.modal.body = "Please enter a valid non-empty name for the new Team!!";
      this.showModal(this.modal);
    }
  }

  showModal(modal:Modal) {
    this.modal = modal;
    this.modal.isShow = true;
  }
  hideModal(modal:Modal) {
    this.modal.isShow = false;
  }

  private navigateToProjectList() {
    this.router.navigateByUrl("/projectList");
  }

  private navigateToProjectDetails(projectId:string) {
    this.router.navigate(["/project", projectId]).then( (e) => {
      if (e) {
        console.log("Navigation is successful!");
      } else {
        console.log("Navigation has failed!");
      }
    });
  }

  // not required as using routerLink from html
  /* navigateToTaskDashboard() {
    this.router.navigate(["/dashboard", this.projectId]).then( (e) => {
      if (e) {
        console.log("Navigation is successful!");
      } else {
        console.log("Navigation has failed!");
      }
    });
  } */

  private getNewProp(prop:string): string {
    return "new" + prop[0].toUpperCase() + prop.substr(1);
  }

  private getIsProp(prop:string): string {
    return "is" + prop[0].toUpperCase() + prop.substr(1);
  }

}
