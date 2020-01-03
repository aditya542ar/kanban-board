import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from 'src/app/config/config.service';
import { Observable } from 'rxjs';
import { TaskQuery } from '../task/task-query';
import { StageQuery } from '../stage/stage-query';
import { TeamQuery } from '../team/team-query';
import { Task } from '../task/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private baseApiUrl = "http:localhost:8080";
  private projectBaseUrl = "/project";
  private userBaseUrl = "/user";
  private teamBaseUrl = "/team";
  private stageBaseUrl = "/stage";
  private taskBaseUrl = "/task";
  constructor(private httpClient:HttpClient, private configService:ConfigService) {
    this.baseApiUrl = this.configService.getConfiguration().webApiBaseUrl;
    this.projectBaseUrl = this.configService.getConfiguration().projectBaseUrl;
    this.userBaseUrl = this.configService.getConfiguration().userBaseUrl;
    this.teamBaseUrl = this.configService.getConfiguration().teamBaseUrl;
    this.stageBaseUrl = this.configService.getConfiguration().stageBaseUrl;
    this.taskBaseUrl = this.configService.getConfiguration().taskBaseUrl;
  }

  fetchAllTasksByProjectId(pid:string):Observable<any> {
    let q:TaskQuery = new TaskQuery();
    q.projectId = pid;
    return this.httpClient.post(this.baseApiUrl + this.taskBaseUrl, q);
  }

  fetchAllTasksByProjectIdAndOwnerId(pid:string, uid:string):Observable<any> {
    let q:TaskQuery = new TaskQuery();
    q.projectId = pid;
    q.ownerId = uid;
    return this.httpClient.post(this.baseApiUrl + this.taskBaseUrl, q);
  }

  fetchAllStagesByProjectId(pid:string):Observable<any> {
    let q:StageQuery = new StageQuery();
    q.projectId = pid;
    return this.httpClient.post(this.baseApiUrl + this.stageBaseUrl, q);
  }

  fetchAllTeamsByProjectId(pid:string):Observable<any> {
    let q:TeamQuery = new TeamQuery();
    q.projectId = pid;
    return this.httpClient.post(this.baseApiUrl + this.teamBaseUrl, q);
  }

  fetchUsersByTeamId(teamId:string):Observable<any> {
    return this.httpClient.get(this.baseApiUrl + this.teamBaseUrl + "/" + teamId + "/users");
  }

  updateTaskById(taskId:string, task:Task):Observable<any> {
    return this.httpClient.put(this.baseApiUrl + this.taskBaseUrl + "/" + taskId + "/update", task);
  }

  deleteTaskById(taskId:string):Observable<any> {
    return this.httpClient.delete(this.baseApiUrl + this.taskBaseUrl + "/" + taskId);
  }

  createNewTask(task:Task):Observable<any> {
    return this.httpClient.post(this.baseApiUrl + this.taskBaseUrl + "/create", task);
  }
}
