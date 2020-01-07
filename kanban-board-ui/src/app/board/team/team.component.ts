import { Component, OnInit } from '@angular/core';
import { UtilService } from 'src/app/util/util.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ProjectService } from '../service/project.service';
import { Team } from './team';
import { User } from '../user/user';
import { Modal } from '../util/modal';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {

  projectId:string;
  teams:Array<Team>;
  allTeam:Team = new Team({"id": "allTeam", "name": "All Team"});
  users:Array<User>;
  currTeam:Team = new Team();
  showTeam:boolean = false;
  showUser:boolean = false;
  loadingUser:boolean = false;
  savingUser:boolean = false;
  noTeam:boolean = false;
  showSearchUserUl:boolean = false;
  addUserToTeam:Team = new Team();
  newUser:User;
  newUserId:string;
  searchUsers:Array<User> = new Array<User>();
  loadingUserSearch:boolean = false;
  notificationMessage:string = "";
  showSuccessNotification:boolean = false;
  showFailureNotification:boolean = false;
  removingUser:boolean = false;

  modal:Modal = new Modal();

  constructor(private projectService:ProjectService, 
    private route: ActivatedRoute, 
    private router:Router,
    private utilService:UtilService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      console.log(params.get('id'));
      this.projectId = params.get("id");
      if(this.projectId) {
        this.loadTeamDetails(this.projectId);
      } else {
        this.navigateToProjectList();
      }
    });
    // Show Project Drop down in header
    this.utilService.showProjectDropDown();
    this.utilService.currPage = "project";
    this.utilService.listenToCurrentProjectChanged()
    .subscribe(
      (projectId) => {
        this.navigateToTeam(projectId);
      }
    );
  }

  loadTeamDetails(projectId:string) {
    this.projectService.fetchTeamsByProjectIds([projectId]).subscribe(
      (teams:Array<Team>) => {
        this.teams = teams;
        if(teams.length > 0) {
          this.fetchUsersForAllTeam();
          this.currTeam = this.allTeam;
          this.showTeam = true;
          this.showUser = true;
        } else {
          this.noTeam = true;
        }
        console.log("teams", teams);
      }
    )
  }

  fetchUsersForAllTeam() {
    this.users = new Array<User>();
    console.log("fetchForAllTeams", this.teams);
    this.teams.forEach((team:Team) => {
      console.log(team.name);
      this.projectService.fetchUsersByTeamId(team.id)
      .subscribe((users:Array<User>) => {
        users.forEach((u) => {
          if(!this.checkUserExistsInArrayById(u.id, this.users)) this.users.push(u);
        });
        console.log("users", this.users);
      });
    });
  }

  checkUserExistsInArrayById(id:string, users:Array<User>):boolean {
    return users.some(el => el.id == id);
  }

  fetchUsersForTeam(team:Team) {
    this.users = new Array<User>();
    this.projectService.fetchUsersByTeamId(team.id)
      .subscribe((users:Array<User>) => {
        this.users = users;
      });
  }

  updateCurrTeam() {
    if(this.currTeam.id == "allTeam") {
      this.fetchUsersForAllTeam();
    } else
      this.fetchUsersForTeam(this.currTeam);
  }

  startAddUser() {
    if(this.currTeam.id == "allTeam")
      this.addUserToTeam = this.teams[0];
    else 
      this.addUserToTeam = this.currTeam;
    this.newUser = undefined;
    this.newUserId = "";
    this.loadingUserSearch = true;
    this.showSuccessNotification = false;
    this.fetchUsersForTeam(this.addUserToTeam);
    this.modal.name = "addUser";
    this.showModal(this.modal);
  }

  cancelAddUser() {
    this.addUserToTeam = new Team();
    this.loadingUserSearch = false;
    this.hideModal();
  }

  selectTeam() {
    this.fetchUsersForTeam(this.addUserToTeam);
    this.searchUsers = [];
  }

  searchUser(userId:string) {
    this.showSearchUserUl = false;
    if(userId && userId.trim().length > 0) {
      this.projectService.fetchUserByUserIdOrNameLike(userId.trim())
      .subscribe((users:Array<User>) => {
        this.searchUsers = users.filter((u) => this.users.indexOf(u) == -1);
        this.showSearchUserUl = true;
      });
    }
  }

  setNewUser(user:User) {
    this.newUser = user;
    this.showSearchUserUl = false;
  }

  saveAddUser() {
    this.showSuccessNotification = false;
    this.showFailureNotification = false;
    console.log("newUser", this.newUser, "addUserToTeam", this.addUserToTeam);
    this.projectService.addUserToTeam(this.addUserToTeam.id, this.newUser).subscribe(
      () => {
        this.notificationMessage = "User " + this.newUser.userId + ", successfully added to team " 
              + this.addUserToTeam.name;
        this.showSuccessNotification = true;
        this.updateCurrTeam();
        this.hideModal();
      }, (err) => {
        this.notificationMessage = "Unable to add User " + this.newUser.userId + ", to team " 
              + this.addUserToTeam.name + ". Please try again later..!!";
        this.showFailureNotification = true;
        this.updateCurrTeam();
        this.hideModal();
      }
    )
  }

  removeUserFromTeam(user:User, currTeam:Team) {
    this.removingUser = true;
    user["isRemoving"] = true;
    this.showSuccessNotification = false;
    this.showFailureNotification = false;
    this.projectService.removeUserFromTeam(currTeam.id, user.id).subscribe(
      () => {
        setTimeout(() => {
          this.removingUser = false;
          this.notificationMessage = "User " + user.userId + ", successfully removed from team " 
              + currTeam.name;
          this.showSuccessNotification = true;
          this.updateCurrTeam();
          this.hideModal();
        }, 3000);
      }, (err) => {
        this.notificationMessage = "Unable to remove User " + user.userId + ", from team " 
              + currTeam.name + ". Please try again later..!!";
        this.showFailureNotification = true;
        this.updateCurrTeam();
        this.hideModal();
      }
    )
  }

  private navigateToProjectList() {
    this.router.navigateByUrl("/project-list");
  }

  private navigateToTeam(projectId:string) {
    this.router.navigate(["/project", projectId, "/user-list"]).then( (e) => {
      if (e) {
        console.log("Navigation is successful!");
      } else {
        console.log("Navigation has failed!");
      }
    });
  }

  showModal(modal:Modal) {
    this.modal = modal;
    this.modal.isShow = true;
  }

  hideModal() {
    this.modal.isShow = false;
  }

}
