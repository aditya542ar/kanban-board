import { Injectable, EventEmitter } from '@angular/core';
import { Project } from '../board/project/project';
import { User } from '../board/user/user';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  private isShowProjectDropDownFlag:boolean = false;
  private projectDropDownList:Array<Project> = new Array<Project>();
  private reloadProjectList$:EventEmitter<any> = new EventEmitter<any>();
  public currProject:string;
  private loggedInUser:User;

  constructor() { }

  showProjectDropDown():void {
    this.isShowProjectDropDownFlag = true;
  }

  hideProjectDropDown():void {
    this.isShowProjectDropDownFlag = false;
  }

  public isShowProjectDropDown():boolean {
    return this.isShowProjectDropDownFlag;
  }

  setProjectDropDownList(projectList:Array<Project>) {
    this.projectDropDownList = projectList;
    console.log("setProjectDropDownList", projectList);
    //workaround to set a random loggedIn user, untill the Login logic is build
    if(projectList) {
      let user:User = new User();
      user.id = projectList[0].ownerId;
      user.firstName = "Dummy";
      user.lastName = "Dumb";
      user.userId = "dummy.d";
      this.setLoggedInUser(user);
    }
  }

  getProjectDropDownList():Array<Project> {
    return this.projectDropDownList;
  }

  emitReloadProjectList() {
    this.reloadProjectList$.emit(true);
  }

  getReloadProjectList() {
    return this.reloadProjectList$;
  }

  setLoggedInUser(user:User) {
    this.loggedInUser = user;
  }

  getLoggedInUser() {
    return this.loggedInUser;
  }
  
}
