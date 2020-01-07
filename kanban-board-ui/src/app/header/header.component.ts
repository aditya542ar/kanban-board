import { Component, OnInit } from '@angular/core';
import { UtilService } from '../util/util.service';
import { Router } from '@angular/router';
import { Project } from '../board/project/project';
import { ProjectService } from '../board/service/project.service';
import { AuthService } from '../auth/auth.service';
import { User } from '../board/user/user';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public util:UtilService, 
    private auth:AuthService,
    private router:Router, 
    private projectService:ProjectService) { }

  ngOnInit() {
    if(this.auth.isUserLoggedIn()) {
      console.log("header -> is Logged In user: true");
      if(!!this.util.getLoggedInUser()) {
        // user is already there in the util service
        console.log("logged in user details is already there in the util service");
      } else {
        console.log("Logged in user is not available in util service..");
        this.auth.revalidateUser(this.auth.getToken()).subscribe(
          (res:any) => {
            this.auth.doLogin(res.userId, res.jwtToken);
            this.fetchUserByUserId(res.userId);
          }
        )
      }
    }

    // listen for the presence of loggedIn user in util service
    this.util.gotLoggedInUser().subscribe(
      (data:any) => {
        this.loadAllProjectList(data.loggedInUser);
    });

    // listen for reload of project list in util service,
    // this will happen when user creates a new project
    this.util.getReloadProjectList().subscribe((data) => {
      this.loadAllProjectList();
    });
  }

  fetchUserByUserId(userId:string) {
    this.auth.fetchUserByUserId().subscribe(
      (user:Array<User>) => {
        console.log("user", user);
        this.util.setLoggedInUser(user[0]);
      }
    );
  }

  loadAllProjectList(user?:User) {
    if(!user) user = this.util.getLoggedInUser();
    this.projectService.fetchAllProjectsByUserId(user).subscribe(
      (res:Array<Project>) => {
        console.log(res);
        this.util.setProjectDropDownList(res);
      });
  }

  changeProjectDropDpwn(projectId:string) {
    this.util.currProject = projectId;
    this.util.emitCurrentProjectChanged(projectId);
    // this.router.navigate(["/project", projectId]).then( (e) => {
    //   if (e) {
    //     console.log("Navigation is successful!");
    //   } else {
    //     console.log("Navigation has failed!");
    //   }
    // });
  }

  doLogout() {
    this.util.setLoggedInUser(undefined);
    this.auth.doLogout();
    this.navigateToLoginPage();
  }

  navigateToLoginPage() {
    this.router.navigateByUrl("/login");
  }

}
