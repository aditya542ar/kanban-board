import { Component, OnInit, ElementRef } from '@angular/core';
import { UtilService } from 'src/app/util/util.service';
import { User } from 'src/app/board/user/user';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user:User;
  showUserDetail:boolean = false;
  isEdit:boolean = false;
  isChangePassword:boolean = false;
  newFirstName:string;
  newLastName:string;
  newProfilePic:any;
  newProfilePicStr:string;
  newPassword:string;
  confirmPassword:string;

  fileReader:FileReader = new FileReader();

  // flags for validation
  invalidFirstName:boolean = false;
  invalidLastName:boolean = false;
  invalidPassword:boolean = false;
  mismatchPassword:boolean = false;

  constructor(private util:UtilService, private auth:AuthService) { }

  ngOnInit() {
    this.util.showSpinner();
    this.util.gotLoggedInUser().subscribe(
      (data) => {
        this.user = data.loggedInUser;
        this.loadUserData();
      }
    )
  }

  loadUserData() {
    this.showUserDetail = true;
    this.util.hideSpinner();
  }

  startEdit() {
    this.newFirstName = this.user.firstName;
    this.newLastName = this.user.lastName;
    this.isEdit = true;
    this.isChangePassword = false;
  }

  cancelEdit() {
    this.isEdit = false;
  }

  saveEdit() {
    console.log("saveEdit");
    this.util.showSpinner();
    let updatedUser:User = new User(this.user);
    updatedUser.firstName = this.newFirstName;
    updatedUser.lastName = this.newLastName;
    updatedUser.profilePic = this.newProfilePicStr ? this.newProfilePicStr : this.user.profilePic; 
    // have to sent password as null
    updatedUser.password = undefined;
    this.auth.updateUser(updatedUser).subscribe(
      () => {
        this.auth.fetchUserByUserId(this.user.userId).subscribe(
          (users:Array<User>) => {
            if(users && users[0]) {
              this.util.setLoggedInUser(users[0]);
              this.user = users[0];
              this.cancelEdit();
              this.util.hideSpinner();
            }
          }
        )
      }, (err) => {
        console.log(err);
        this.util.hideSpinner();
      }
    )
  }

  onFileChange(event) {
    console.log("newProfilePic", this.newProfilePic);
    console.log(event);
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(event.target.files[0]); // read file as data url
      
      reader.onload = (event) => { // called once readAsDataURL is completed
        let profilePicStr = (event.target as FileReader).result as string;
        this.setProfilePic(profilePicStr);
      }
    }
  }

  setProfilePic(proPicStr) {
    console.log("proPicStr", proPicStr);
    this.newProfilePicStr = proPicStr;
  }

  saveChange() {
    if(this.isEdit) {
      this.saveEdit();
    } else if(this.isChangePassword) {
      this.saveChangePassword();
    }
  }

  cancelChange() {
    if(this.isEdit) {
      this.cancelEdit();
    } else if(this.isChangePassword) {
      this.cancelChangePassword();
    }
  }

  startChangePassword() {
    this.newPassword = "";
    this.confirmPassword = "";
    this.isChangePassword = true;
  }

  cancelChangePassword() {
    this.isChangePassword = false;
  }

  saveChangePassword() {
    console.log("change password");
    this.util.showSpinner();
    let updatedUser:User = new User(this.user);
    updatedUser.password = this.newPassword;
    this.auth.updateUser(updatedUser).subscribe(
      () => {
        this.auth.fetchUserByUserId(this.user.userId).subscribe(
          (users:Array<User>) => {
            if(users && users[0]) {
              this.util.setLoggedInUser(users[0]);
              this.user = users[0];
              this.cancelChangePassword();
              this.util.hideSpinner();
            }
          });
      }, (err) => {
        console.log(err);
        this.util.hideSpinner();
      }
    )
  }

  validateFirstName() {
    let firstNamePattern = /^[a-zA-Z ]+$/;
    if(!this.newFirstName || this.newFirstName.trim().length < 2 
        || this.newFirstName.trim().length > 20 || !firstNamePattern.test(this.newFirstName.trim())) {
      this.invalidFirstName = true;
    } else {
      this.invalidFirstName = false;
    }
  }

  validateLastName() {
    let lastNamePattern = /^[a-zA-Z ]+$/;
    if(!this.newLastName || this.newLastName.trim().length < 2 
        || this.newLastName.trim().length > 20 || !lastNamePattern.test(this.newLastName.trim())) {
      this.invalidLastName = true;
    } else {
      this.invalidLastName = false;
    }
  }

  validatePassword() {
    if(!this.newPassword || this.newPassword.length < 4 || this.newPassword.length > 100) {
      this.invalidPassword = true;
    } else {
      this.invalidPassword = false;
    }
  }

  validateConfirmPassword() {
    if(this.newPassword != this.confirmPassword) {
      this.mismatchPassword = true;
    } else {
      this.mismatchPassword = false;
    }
  }

  validateForm():boolean {
    if(this.isEdit) {
      return !(this.invalidFirstName || this.invalidLastName);
    } else if(this.isChangePassword) {
      return !(this.invalidPassword || this.mismatchPassword);
    } else return false;
  }

}
