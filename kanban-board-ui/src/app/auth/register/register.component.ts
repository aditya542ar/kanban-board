import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/board/user/user';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { UtilService } from 'src/app/util/util.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user:User = new User();
  message:string = "";
  registerSuccess:boolean = false;
  registerFail:boolean = false;
  constructor(private auth:AuthService, private router:Router, private util:UtilService) { }

  ngOnInit() {
    this.resetForm();
    this.util.hideProjectDropDown();
    this.util.currPage = "register";
    this.util.hideSpinner();
  }

  doRegister() {
    // this.isSubmitted = true;
    this.util.showSpinner();
    this.validateUserId();
    this.validateFirstName();
    this.validateLastName();
    this.validatePassword();
    this.validateConfirmPassword();
    if(this.validateForm()) {
      this.registerSuccess = false;
      this.registerFail = false;
      let newUser:User = new User(this.user);
      console.log(newUser);
      this.auth.registerUser(newUser).subscribe(
        (user:User) => {
          this.registerSuccess = true;
          this.message = "User registered successfully..";
          this.util.hideSpinner();
          setTimeout(() => {
            this.router.navigate(["/login"]);
          }, 3000);
        }, (err) => {
          console.log(err);
          if(err.status === 400)
            this.message = err.error.message;
          else
            this.message = "Unable to register user.. Please try again later with valid data..";
          setTimeout(() => {
            this.registerFail = true;
            this.util.hideSpinner();
          }, 3000);
        }
      );
    } else {
      console.log("Invalid Form Data submitted");
      this.registerFail = true;
      this.message = "Invalid Form data submitted.. Please try again with valid data..";
      this.util.hideSpinner();
    }
    
  }

  validateUserId() {
    let idPattern = /^[a-zA-Z0-9.]+$/;
    if(!this.user.userId || this.user.userId.trim().length < 4 
        || this.user.userId.trim().length > 20 || !idPattern.test(this.user.userId.trim())) {
      this.user["invalidUserId"] = true;
    } else {
      this.user["invalidUserId"] = false;
    }
  }

  validateFirstName() {
    let firstNamePattern = /^[a-zA-Z ]+$/;
    if(!this.user.firstName || this.user.firstName.trim().length < 2 
        || this.user.firstName.trim().length > 20 || !firstNamePattern.test(this.user.firstName.trim())) {
      this.user["invalidFirstName"] = true;
    } else {
      this.user["invalidFirstName"] = false;
    }
  }

  validateLastName() {
    let lastNamePattern = /^[a-zA-Z ]+$/;
    if(!this.user.lastName || this.user.lastName.trim().length < 2 
        || this.user.lastName.trim().length > 20 || !lastNamePattern.test(this.user.lastName.trim())) {
      this.user["invalidLastName"] = true;
    } else {
      this.user["invalidLastName"] = false;
    }
  }

  validatePassword() {
    if(!this.user.password || this.user.password.length < 4 || this.user.password.length > 100) {
      this.user["invalidPassword"] = true;
    } else {
      this.user["invalidPassword"] = false;
    }
  }

  validateConfirmPassword() {
    if(this.user.password != this.user["password2"]) {
      this.user["mismatchPassword"] = true;
    } else {
      this.user["mismatchPassword"] = false;
    }
  }

  validateForm():boolean {
    if(this.user["invalidPassword"] || this.user["mismatchPassword"] 
      || this.user["invalidUserId"] || this.user["invalidFirstName"]
      || this.user["invalidLastName"]) {
        return false;
    } else {
      return true;
    }
  }

  resetForm() {
    this.registerSuccess = false;
    this.registerFail = false;
    this.user = new User();
    this.user["invalidPassword"] = true;
    this.user["mismatchPassword"] = true;
    this.user["invalidUserId"] = true;
    this.user["invalidFirstName"] = true;
    this.user["invalidLastName"] = true;
  }

}
