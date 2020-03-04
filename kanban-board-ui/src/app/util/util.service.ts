import { Injectable, EventEmitter } from '@angular/core';
import { Project } from '../board/project/project';
import { User } from '../board/user/user';
import { Observable, Observer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  private isShowProjectDropDownFlag:boolean = false;
  private projectDropDownList:Array<Project> = new Array<Project>();
  private reloadProjectList$:EventEmitter<any> = new EventEmitter<any>();
  public currProject:string;
  public currPage:string;
  private loggedInUser:User;
  private gotProjectAndLoggedInUser$:EventEmitter<any> = new EventEmitter<any>();
  private currentProjectChanged$:EventEmitter<string> = new EventEmitter<string>();
  private loggedInUserSet$:EventEmitter<string> = new EventEmitter<string>();
  
  // flag for spinner
  private spinner:boolean = true;

  constructor() { }

  // getter & setter for spinner
  showSpinner() {
    setTimeout(() => {
      this.spinner = true;
    }, 10);
  }

  hideSpinner() {
    setTimeout(() => {
      this.spinner = false;
    }, 10);
  }

  isSpinner() {
    return this.spinner;
  }

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
    if(projectList && projectList.length > 0) this.currProject = projectList[0].id;
    this.gotProjectAndLoggedInUser$.emit("project");
    console.log("setProjectDropDownList", projectList);
    //workaround to set a random loggedIn user, untill the Login logic is build
    // if(projectList) {
    //   let user:User = new User();
    //   user.id = projectList[0].ownerId;
    //   user.firstName = "Dummy";
    //   user.lastName = "Dumb";
    //   user.userId = "dummy.d";
    //   this.setLoggedInUser(user);
    // }
    // setTimeout(() => {
    //   this.gotProjectAndLoggedInUser$.emit("loggedInUser");
    // }, 1000);
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
    console.log("setting logged in user");
    this.loggedInUser = user;
    if(user) {
      this.loggedInUserSet$.emit(user.userId);
      this.gotProjectAndLoggedInUser$.emit("loggedInUser");
    }
  }

  getLoggedInUser() {
    return this.loggedInUser;
  }

  gotProjectAndLoggedInUser():Observable<any> {
    return Observable.create((observer: Observer<any>) => {
      if(this.projectDropDownList && this.loggedInUser) {
        observer.next({"project": this.projectDropDownList, "projectId": this.currProject, "loggedInUser": this.loggedInUser});
      } else {
        this.gotProjectAndLoggedInUser$.subscribe((name) => {
          if(this.projectDropDownList && this.loggedInUser) {
            observer.next({"project": this.projectDropDownList, "projectId": this.currProject, "loggedInUser": this.loggedInUser});
          }
        });
      }
    });
  }

  gotLoggedInUser():Observable<any> {
    return Observable.create((observer: Observer<any>) => {
      if(this.loggedInUser) {
        observer.next({"loggedInUser": this.loggedInUser});
      } else {
        this.loggedInUserSet$.subscribe((name) => {
          if(this.loggedInUser) {
            observer.next({"loggedInUser": this.loggedInUser});
          }
        });
      }
    });
  }

  emitCurrentProjectChanged(projectId:string) {
    this.currentProjectChanged$.emit(projectId);
  }

  listenToCurrentProjectChanged() {
    return this.currentProjectChanged$;
  }

  listenToLoggedInUserSet() {
    return this.loggedInUserSet$;
  }
  
}
