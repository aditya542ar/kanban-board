import { Injectable, EventEmitter } from '@angular/core';
import { Project } from '../board/project/project';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  private isShowProjectDropDownFlag:boolean = false;
  private projectDropDownList:Array<Project> = new Array<Project>();
  private reloadProjectList$:EventEmitter<any> = new EventEmitter<any>();
  public currProject:string;

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
  
}
