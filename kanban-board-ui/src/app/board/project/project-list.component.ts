import { Component, OnInit, EventEmitter, Pipe, Injectable, PipeTransform } from '@angular/core';
import { ProjectService } from '../service/project.service';
import { Project } from './project';
import { User } from '../user/user';
import { Team } from '../team/team';
import { Stage } from '../stage/stage';
import { Task } from '../task/task';
import { EmitAction } from '../util/emit-action';
import { UtilService } from 'src/app/util/util.service';

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

  constructor(private projectService:ProjectService,
    private utilService:UtilService) { }

  ngOnInit() {
    this.checkSubscriptionData();
    this.loadAllProjects();
    this.utilService.hideProjectDropDown();
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

}
