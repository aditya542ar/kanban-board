import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { UtilService } from 'src/app/util/util.service';
import { User } from 'src/app/board/user/user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username:string;
  password:string;
  constructor(private authService:AuthService, private util:UtilService, private router:Router) { }

  ngOnInit() {
    this.util.hideProjectDropDown();
    this.util.currPage = "login";
  }

  doLogin() {
    this.authService.authenticateUser(this.username, this.password).subscribe(
      (res:any) => {
        this.authService.doLogin(this.username, res.jwtToken);
        this.authService.fetchUserByUserId(this.username).subscribe(
          (user:Array<User>) => {
            this.util.setLoggedInUser(user[0]);
            // this.navigateToDashBoard();
            this.navigateToProjectList();
          }, (err) => {
            console.log("Error fetching logged in User details..!!");
          }
        );
      }, (err) => {
        console.log("Invalid login..!!!");
      }
    )
  }

  navigateToDashBoard() {
    this.router.navigateByUrl("/dashboard");
  }

  navigateToProjectList() {
    this.router.navigateByUrl("/project-list");
  }

}
