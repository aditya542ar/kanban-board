import { Component, OnInit, EventEmitter, Pipe, Injectable, PipeTransform, ViewChild, ElementRef } from '@angular/core';
import { ProjectService } from '../service/project.service';
import { Project } from './project';
import { User } from '../user/user';
import { Team } from '../team/team';
import { Stage } from '../stage/stage';
import { Task } from '../task/task';
import { EmitAction } from '../util/emit-action';
import { UtilService } from 'src/app/util/util.service';
import { Modal } from '../util/modal';

@Pipe({
  name: 'projectSearch',
  pure: false
})

@Injectable({
  providedIn: 'root'
})
export class ProjectSearchPipe implements PipeTransform {
  transform(iteams:Array<Project>, field:string, value:string): Array<Project> {
    if(!iteams) return [];
    if(!value || value.trim().length == 0) return iteams;
    return iteams.filter(p => {
      if(p[field]) return (p[field] as string).toLowerCase().indexOf(value.toLowerCase()) != -1;
      else return false;
    });
  }
}

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {
  searchByList:Array<string> = new Array<string>("name", "id", "startDate", "endDate");
  currSearchBy:string = this.searchByList[0];
  searchString:string = "";
  showAllProjects:boolean = false;
  allProjects:Array<Project> = new Array<Project>();
  owners:Array<User> = new Array<User>();
  teamsOfProjects:Array<Team> = new Array<Team>();
  stagesOfProjects:Array<Stage> = new Array<Stage>();
  tasksOfProjects:Array<Task> = new Array<Task>();
  private gotSubscriptionData$:EventEmitter<EmitAction> = new EventEmitter<EmitAction>();
  private action:string = "loadAllProjects";
  private gotUserData:boolean = false;
  private gotTeamData:boolean = false;
  private gotStageData:boolean = false;
  private gotTaskData:boolean = false;
  modal:Modal = new Modal();
  newProject:Project;
  createProjectSuccess:boolean = false;
  createProjectFail:boolean = false;
  creatingNewProject:boolean = false;
  popupMsg:string = "";
  @ViewChild("modalBodyDiv")
  modalBodyDiv:ElementRef;

  constructor(private projectService:ProjectService,
    private utilService:UtilService) { }

  ngOnInit() {
    this.checkSubscriptionData();
    this.loadAllProjects();
    this.utilService.hideProjectDropDown();
    this.utilService.currPage = "projectList";
  }

  loadAllProjects():void {
    this.projectService.fetchAllProjects().subscribe(
      (res) => {
        console.log(res);
        this.allProjects = res;
        this.gotSubscriptionData$.emit(new EmitAction("loadAllProjects", true));
        let userIds = this.allProjects.map((p) => p.ownerId);
        console.log("userIds", userIds);
        this.projectService.fetchUserByIds(userIds).subscribe(
          (users) => {
            console.log(users);
            this.owners = users;
            this.gotUserData = true;
            this.gotSubscriptionData$.emit(new EmitAction("loadAllProjects", true));
        });
        let projectIds = this.allProjects.map((p) => p.id);
        this.projectService.fetchTeamsByProjectIds(projectIds)
        .subscribe((teams:Array<Team>) => {
          console.log(teams);
          this.teamsOfProjects = teams;
          this.gotTeamData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadAllProjects", true));
        });
        this.projectService.fetchStagesByProjectIds(projectIds)
        .subscribe((stages:Array<Stage>) => {
          console.log(stages);
          this.stagesOfProjects = stages;
          this.gotStageData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadAllProjects", true));
        });
        // this.projectService.fetchTasksByProjectId(projectIds)
        // .subscribe((tasks:Array<Task>) => {
        //   console.log(tasks);
        //   this.tasksOfProjects = tasks;
        //   this.gotTaskData = true;
        //   this.gotSubscriptionData$.emit(new EmitAction("loadAllProjects", true));
        // });
      });
  }

  checkSubscriptionData() {
    this.gotSubscriptionData$.subscribe((ea:EmitAction) => {
      switch(ea.action){
        case "loadAllProjects":
          console.log("loadAllProjects Case");
          if(this.gotUserData && this.gotTeamData && this.gotStageData 
            // && this.gotTaskData
            ) {
            this.showAllProjects = true;
          }
      }
    });
  }

  getUserById(users:Array<User>, id:string): User {
    return users.filter((user) => user.id == id)[0];
  }

  getTeamsByProjectId(teams:Array<Team>, id:string): Array<Team> {
    return teams.filter((team) => team.projectId == id);
  }

  getStagesByProjectId(stages:Array<Stage>, id:string): Array<Stage> {
    return stages.filter((stage) => stage.projectId == id);
  }

  getTasksByProjectId(tasks:Array<Task>, id:string): Array<Task> {
    return tasks.filter((task) => task.team.projectId == id);
  }

  startCreateProject() {
    this.newProject = new Project();
    this.createProjectSuccess = false;
    this.createProjectFail = false;
    this.creatingNewProject = false;
    this.modal.header = "Create New Project";
    this.showModal(this.modal);
  }

  cancelCreateProject() {
    this.newProject = undefined;
    this.createProjectSuccess = false;
    this.createProjectFail = false;
    this.creatingNewProject = false;
    this.hideModal();
  }

  createNewProject() {
    this.createProjectSuccess = false;
    this.createProjectFail = false;
    let valid = this.validateProjectData();
    if(valid.pass) {
      console.log("valid project data");
      this.creatingNewProject = true;
      this.newProject.ownerId = this.utilService.getLoggedInUser().id;
      this.projectService.createProject(this.newProject).subscribe(
        (project:Project) => {
          this.createProjectSuccess = true;
          this.popupMsg = "Project '" + project.name + "' created successfully.";
          this.modalBodyDiv.nativeElement.scrollTo(0, 0);
          this.utilService.emitReloadProjectList();
          this.loadAllProjects();
          setTimeout(() => {
            this.cancelCreateProject();
          }, 2000);
        }, 
        (err) => {
          this.createProjectFail = true;
          this.popupMsg = "Unable to create new Project '" + this.newProject.name 
                + "'.\n Please try again with correct data.";
          this.creatingNewProject = false;
          this.modalBodyDiv.nativeElement.scrollTo(0, 0);
        }
      );
    } else {
      this.createProjectFail = true;
      this.popupMsg = "Invalid " + valid.field;
      this.creatingNewProject = false;
      //console.log(this.modalBodyDiv.nativeElement);
      this.modalBodyDiv.nativeElement.scrollTo(0, 0);
    }
  }

  validateProjectData():any {
    console.log(this.newProject);
    if(this.newProject.name == undefined || this.newProject.name.trim().length == 0) {
      return { "pass" : false, "field": "name" };
    } else if(this.newProject.startDate == undefined || this.newProject.startDate.trim().length != 10) {
      return { "pass" : false, "field": "startDate" };
    } else if(this.newProject.endDate != undefined && this.newProject.endDate.trim().length != 10) {
      return { "pass" : false, "field": "endDate" };
    } else {
      // check start Date
      try {
        let dateArr:Array<any> = this.newProject.startDate.split("-");
        let startDate = new Date(dateArr[2], dateArr[1], dateArr[0]);
      } catch (error) {
        return { "pass" : false, "field": "startDate" };
      }
      // check end Date
      try {
        if(this.newProject.endDate == undefined) {
          // optional field.. so can be ignored
        } else {
          let dateArr:Array<any> = this.newProject.endDate.split("-");
          let endDate = new Date(dateArr[2], dateArr[1], dateArr[0]);
        }
      } catch (error) {
        return { "pass" : false, "field": "endDate" };
      }
    }
    return { "pass" : true, "field": "all" };
  }

  showModal(modal:Modal) {
    this.modal = modal;
    this.modal.isShow = true;
  }
  hideModal() {
    this.modal.isShow = false;
  }

}
