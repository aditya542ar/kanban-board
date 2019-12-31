import { Component, OnInit } from '@angular/core';
import { UtilService } from '../util/util.service';
import { Router } from '@angular/router';
import { Project } from '../board/project/project';
import { ProjectService } from '../board/service/project.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public util:UtilService, 
    private router:Router, 
    private projectService:ProjectService) { }

  ngOnInit() {
    this.loadAllProjectList();
    this.util.getReloadProjectList().subscribe((data) => {
      this.loadAllProjectList();
    });
  }

  loadAllProjectList() {
    this.projectService.fetchAllProjects().subscribe(
      (res:Array<Project>) => {
        console.log(res);
        this.util.setProjectDropDownList(res);
      });
  }

  navigateToProjectDetail(projectId:string) {
    this.util.currProject = projectId;
    this.router.navigate(["/project", projectId]).then( (e) => {
      if (e) {
        console.log("Navigation is successful!");
      } else {
        console.log("Navigation has failed!");
      }
    });
  }

}
