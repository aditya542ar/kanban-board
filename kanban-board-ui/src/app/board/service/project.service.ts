import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from 'src/app/config/config.service';
import { Project } from '../project/project';
import { Observable } from 'rxjs';
import { User } from '../user/user';
import { UserQuery } from '../user/user-query';
import { TeamQuery } from '../team/team-query';
import { StageQuery } from '../stage/stage-query';
import { TaskQuery } from '../task/task-query';
import { Stage } from '../stage/stage';
import { Team } from '../team/team';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseApiUrl = "http:localhost:8080";
  private projectBaseUrl = "/project";
  private userBaseUrl = "/user";
  private teamBaseUrl = "/team";
  private stageBaseUrl = "/stage";
  private taskBaseUrl = "/task";
  constructor(private httpClient:HttpClient, private configService:ConfigService) {
    this.baseApiUrl = this.configService.getConfiguration().webApiBaseUrl;
  }

  fetchAllProjects(): Observable<any>{
    return this.httpClient.get(this.baseApiUrl + this.projectBaseUrl);
  }

  fetchUserByIds(ids:Array<string>): Observable<any> {
    let q:UserQuery = new UserQuery();
    ids.forEach((id) => q.getIdIn().push(id));
    return this.httpClient.post(this.baseApiUrl + this.userBaseUrl, q);
  }

  fetchTeamsByProjectIds(ids:Array<string>): Observable<any> {
    let q:TeamQuery = new TeamQuery();
    ids.forEach((id) => q.getProjectIdIn().push(id));
    return this.httpClient.post(this.baseApiUrl + this.teamBaseUrl, q);
  }

  fetchStagesByProjectIds(ids:Array<string>): Observable<any> {
    let q:StageQuery = new StageQuery();
    ids.forEach((id) => q.getProjectIdIn().push(id));
    return this.httpClient.post(this.baseApiUrl + this.stageBaseUrl, q);
  }

  fetchTasksByProjectId(id:string): Observable<any> {
    let q:TaskQuery = new TaskQuery();
    q.projectId = id;
    return this.httpClient.post(this.baseApiUrl + this.taskBaseUrl, q);
  }

  fetchTasksCountByProjectId(id:string): Observable<any> {
    return this.httpClient.get(this.baseApiUrl + this.taskBaseUrl + "/count?projectId=" + id);
  }

  fetchTasksCountByStageId(id:string): Observable<any> {
    return this.httpClient.get(this.baseApiUrl + this.taskBaseUrl + "/count?categoryId=" + id);
  }

  fetchTasksCountByTeamId(id:string): Observable<any> {
    return this.httpClient.get(this.baseApiUrl + this.taskBaseUrl + "/count?teamId=" + id);
  }

  fetchAllProjectsByPost(){}

  fetchProjectCount(){}

  createProject(project:Project){
    return this.httpClient.post(this.baseApiUrl + this.projectBaseUrl + "/create", project);
  }

  updateProject(id:string, project:Project): Observable<any> {
    return this.httpClient.put(this.baseApiUrl + this.projectBaseUrl + "/" + id + "/update", project);
  }

  fetchProjectById(projectId:string):Observable<any>{
    return this.httpClient.get(this.baseApiUrl + this.projectBaseUrl + "/" + projectId);
  }

  fetchUserById(id:string): Observable<any> {
    return this.httpClient.get(this.baseApiUrl + this.userBaseUrl + "/" + id);
  }

  fetchUserByUserIdOrNameLike(userId:string): Observable<any> {
    let q:UserQuery = new UserQuery();
    q.userIdLike = "%" + userId + "%";
    q.firstNameLike = "%" + userId + "%";
    q.lastNameLike = "%" + userId + "%";
    return this.httpClient.post(this.baseApiUrl + this.userBaseUrl + "/search", q);
  }

  addStageToProject(stage:Stage): Observable<any> {
    return this.httpClient.post(this.baseApiUrl + this.stageBaseUrl + "/create", stage);
  }

  removeStage(id:string): Observable<any> {
    return this.httpClient.delete(this.baseApiUrl + this.stageBaseUrl + "/" + id);
  }

  renameStage(stage:Stage): Observable<any> {
    return this.httpClient.put(this.baseApiUrl + this.stageBaseUrl + "/" + stage.id + "/update", stage);
  }

  addTeamToProject(team:Team): Observable<any> {
    return this.httpClient.post(this.baseApiUrl + this.teamBaseUrl + "/create", team);
  }

  removeTeam(id:string): Observable<any> {
    return this.httpClient.delete(this.baseApiUrl + this.teamBaseUrl + "/" + id);
  }

  renameTeam(team:Team): Observable<any> {
    return this.httpClient.put(this.baseApiUrl + this.teamBaseUrl + "/" + team.id + "/update", team);
  }

}
