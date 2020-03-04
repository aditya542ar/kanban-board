import { Component, OnInit, EventEmitter, Injectable, Pipe, PipeTransform } from '@angular/core';
import { UtilService } from 'src/app/util/util.service';
import { faSortAmountDown, faSortAmountDownAlt, faPlus, 
  faEdit, faEye, faExchangeAlt, faTrashAlt, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { Stage } from '../stage/stage';
import { User } from '../user/user';
import { TaskService } from '../service/task.service';
import { Task } from './task';
import { Team } from '../team/team';
import { EmitAction } from '../util/emit-action';
import { Modal } from '../util/modal';

@Pipe({
  name: 'taskSearch',
  pure: false
})

@Injectable({
  providedIn: 'root'
})
export class TaskSearchPipe implements PipeTransform {
  transform(items:Array<Task>, field:string, value:string): Array<Task> {
    console.log("search value", items, field, value);
    if(!items) return [];
    if(!value || value.trim().length == 0) return items;
    return items.filter(p => {
      let t = p;
      field.split(".").forEach((f) => {
        t = t[f];
      });
      if(t) return (t as unknown as string).toLowerCase().indexOf(value.toLowerCase()) != -1;
      else return false;
    });
  }
}

@Pipe({
  name: 'taskSort',
  pure: false
})

@Injectable({
  providedIn: 'root'
})
export class TaskSortPipe implements PipeTransform {
  transform(items:Array<Task>, prop:string, order:string): Array<Task> {
    if(!items) return [];
    if(!prop || prop.trim().length == 0) return items;
    if(!order) order = 'asc';
    if(order == 'asc' ) {
      return items.sort((a, b) => {
        let t1 = a;
        let t2 = b;
        prop.split(".").forEach((pr) => {
          t1 = t1[pr];
          t2 = t2[pr];
        });
        return t1 > t2 ? 1 : t1 === t2 ? 0 : -1;
      });
    }
    if(order == 'desc' ) {
      return items.sort((a, b) => {
        let t1 = a;
        let t2 = b;
        prop.split(".").forEach((pr) => {
          t1 = t1[pr];
          t2 = t2[pr];
        });
        return t1 < t2 ? 1 : t1 === t2 ? 0 : -1;
      });
    }
  }
}

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {
  searchByMap:any = {
    "name": "name", 
    "category": "category.name", 
    "team": "team.name",
    "owner": "owner.userId"
  };
  currSearchByStr:string = "name";
  currSearchBy:string = this.searchByMap[this.currSearchByStr];
  
  sortByMap:any = {
    "name": "name", 
    "category": "category.name", 
    "team": "team.name", 
    "owner": "owner.userId", 
    "priority": "priority", 
  };
  currSortByStr:string = "name";
  currSortBy:string = this.sortByMap[this.currSortByStr];
  currSortOrder:string = "asc";
  currStage:Stage;
  allStageFilter:Stage = new Stage();
  currTask:Task = new Task();

  gotSubscriptionData$:EventEmitter<EmitAction> = new EventEmitter<EmitAction>();
  gotTaskData:boolean = false;
  gotTeamData:boolean = false;
  gotStageData:boolean = false;
  gotUserData:boolean = false;
  showTasks:boolean = false;

  allTasks:Array<Task>;
  allTeams:Array<Team>;
  allStages:Array<Stage>;
  allUsers:Array<User>;

  modal:Modal = new Modal();

  isSavingTask:boolean = false;
  isNewTask:boolean = false;

  icons:any = {
    "faSortAmountDown": faSortAmountDown,
    "faSortAmountDownAlt": faSortAmountDownAlt,
    "faPlus": faPlus,
    "faEdit": faEdit,
    "faEye": faEye,
    "faExchangeAlt": faExchangeAlt,
    "faTrashAlt": faTrashAlt,
    "faAngleUp": faAngleUp
  };
  constructor(public utilService:UtilService, private taskService:TaskService) { }

  ngOnInit() {
    // Show Project Drop down in header
    this.utilService.showSpinner();
    this.utilService.showProjectDropDown();
    this.utilService.currPage = "task-dashboard";
    this.checkSubscriptionData();
    this.utilService.listenToCurrentProjectChanged()
    .subscribe(
      (projectId) => {
        this.utilService.showSpinner();
        this.loadAllTasksByProjectAndUser(projectId, this.utilService.getLoggedInUser());
      }
    );
    this.utilService.gotProjectAndLoggedInUser()
    .subscribe(
      (data:any) => {
        this.loadAllTasksByProjectAndUser(data.projectId, data.loggedInUser);
      }
    );
  }

  checkSubscriptionData() {
    this.gotSubscriptionData$.subscribe((ea:EmitAction) => {
      switch(ea.action){
        case "loadTasks":
          console.log("loadTasks Case");
          if(this.gotTaskData && this.gotTeamData && this.gotStageData && this.gotUserData) {
            this.showTasks = true;
            this.utilService.hideSpinner();
          }
          console.log("showTasks", this.showTasks);
      }
    });
  }

  resetAllDataBeforeLoading() {
    this.currSearchByStr = "name";
    this.currSearchBy = this.searchByMap[this.currSearchByStr];
    this.currSortByStr = "name";
    this.currSortBy = this.sortByMap[this.currSortByStr];
    this.currSortOrder = "asc";
    this.currStage = undefined;
    this.currTask = new Task();
    this.gotTaskData = false;
    this.gotTeamData = false;
    this.gotStageData = false;
    this.gotUserData = false;
    this.showTasks = false;

    this.allTasks = undefined;
    this.allTeams = undefined;
    this.allStages = undefined;
    this.allUsers = undefined;

    this.modal = new Modal();

    this.isSavingTask = false;
    this.isNewTask = false;
  }

  loadAllTasksByProjectAndUser(projectId:string, loggedInUser:User) {
    // Reset all data before loading a new project tasks
    this.resetAllDataBeforeLoading();
    try {
      // fetch teams for logged in user
      this.taskService.fetchTeamsByUserId(loggedInUser.id).subscribe(
        (teams:Array<Team>) => {
          this.allTeams = teams;
          this.gotTeamData = true;
          this.gotSubscriptionData$.emit(new EmitAction("loadTasks", true));
          let c:number = 0;

          // fetch users for each team
          this.allTeams.forEach(
            (team:Team) => {
              this.taskService.fetchUsersByTeamId(team.id).subscribe(
                (users:Array<User>) => {
                  c++;
                  team["users"] = users;
                  users.forEach((user:User) => {
                    this.allUsers = this.allUsers || new Array<User>();
                    if(this.allUsers.indexOf(user) == -1) 
                      this.allUsers.push(user);
                  });
                  if(c == this.allTeams.length) {
                    console.log("allUsers", this.allUsers);
                    this.gotUserData = true;
                    this.gotSubscriptionData$.emit(new EmitAction("loadTasks", true));
                  }
              }, (err) => {
                throw err;
              });
          });
          
          let teamIds:Array<string> = this.allTeams.map((t) => t.id);

          // fetch tasks for all teams
          this.taskService.fetchAllTasksByTeamIds(teamIds)
          .subscribe(
            (tasks:Array<Task>) => {
              this.allTasks = tasks;
              this.gotTaskData = true;
              this.gotSubscriptionData$.emit(new EmitAction("loadTasks", true));

              // fetch all stages of project
              this.fetchAllStagesByProjectId(projectId);
            }, (err) => {
              throw err;
            });
        }, (err) => {
          throw err;
        });

    } catch (error) {
      this.modal.name = "msgModal";
      this.modal.body = {
        "status": "fail",
        "data": "Unable to load Tasks. Please try again later...!!"
      };
      this.utilService.hideSpinner();
      this.showModal(this.modal);
    }
    //this.taskService.fetchAllTasksByProjectIdAndOwnerId(projectId, loggedInUser.id)
    //this.taskService.fetchAllTasksByProjectId(projectId)
    
  }

  fetchAllStagesByProjectId(projectId) {
    this.taskService.fetchAllStagesByProjectId(projectId)
    .subscribe(
      (stages:Array<Stage>) => {
        this.allStages = stages;
        this.gotStageData = true;
        if(this.allStages && this.allStages.length > 0) this.currStage = this.allStages[0];
        this.gotSubscriptionData$.emit(new EmitAction("loadTasks", true));

        // testing max version per each task & stage
        this.allTasks.forEach((task) => {
          this.assignTaskStageChange(task);
          console.log("task", task);
        });

      }, (err) => {
        throw err;
      });
  }

  assignTaskStageChange(task:Task) {
    this.allStages.forEach((stage) => {
      let maxVersion = task.taskStageChanges.filter((tsc) => tsc.stage.id == stage.id).map((s) => s.version).reduce((a, b) => Math.max(a, b), 0);
      if(maxVersion == 0) task[stage.id] = {};
      else task[stage.id] = task.taskStageChanges.filter((tsc) => tsc.stage.id == stage.id && tsc.version == maxVersion)[0];
    });
  }

  setCurrStage(stage:Stage) {
    this.currStage = stage;
  }

  showModal(modal:Modal) {
    this.modal = modal;
    this.modal.isShow = true;
  }
  hideModal() {
    this.modal.isShow = false;
  }

  showTaskDetail(task:Task) {
    this.currTask = Object.assign({}, JSON.parse(JSON.stringify(task)));
    this.modal.name = "viewTask";
    this.showModal(this.modal);
  }

  startEditTask(task:Task) {
    // this.currTask = task;
    this.currTask = Object.assign({}, JSON.parse(JSON.stringify(task)));
    console.log("currTask", this.currTask);
    console.log("allStages", this.allStages);
    this.modal.name = "editTask";
    this.showModal(this.modal);
  }

  checkPriority(task:Task) {
    if(Number(task.priority) == NaN) {
      task.priority = 0;
    } else if(task.priority >= 0 && task.priority <= 100) {
      return true;
    } else if(task.priority > 100) {
      task.priority = 100;
    } else if(task.priority < 0) {
      task.priority = 0;
    }
    return false;
  }

  isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
  }

  saveEditTask() {
    this.utilService.showSpinner();
    console.log(this.currTask);
    console.log("new Task", new Task(this.currTask));
    if(this.isNewTask) {
      this.saveNewTask();
      return;
    }
    this.isSavingTask = true;
    this.hideModal();
    let task:Task = new Task(this.currTask);
    this.taskService.updateTaskById(task.id, task).subscribe(
      (task:Task) => {
        this.allTasks.every((t, i) => {
          console.log(t, i);
          if(t.id == task.id) {
            this.assignTaskStageChange(task);
            this.allTasks.splice(i, 1, task);
            return false;
          }
          return true;
        });
        setTimeout(() => {
          this.isSavingTask = false;
          this.utilService.hideSpinner();
        }, 3000);
        console.log("allTask:", this.allTasks);
      }, (err) => {
        this.modal.name = "msgModal";
        this.modal.body = {
          "status": "fail",
          "data": "Unable to edit Task. Please try again later...!!"
        };
        this.isSavingTask = false;
        this.utilService.hideSpinner();
        this.showModal(this.modal);
      });
  }

  cancelEditTask() {
    this.currTask = new Task(); 
    this.hideModal();
  }

  startMoveTask(task:Task) {
    // this.currTask = task;
    this.currTask = Object.assign({}, JSON.parse(JSON.stringify(task)));
    console.log("currTask", this.currTask);
    console.log("allStages", this.allStages);
    this.modal.name = "moveTask";
    this.modal.body = task.category;
    this.showModal(this.modal);
  }

  deleteTask(task:Task) {
    this.utilService.showSpinner();
    this.currTask = new Task(task);
    this.isSavingTask = true;
    this.hideModal();
    this.taskService.deleteTaskById(task.id).subscribe(
      () => {
        setTimeout(() => {
          this.allTasks.every((t, i) => {
            console.log(t, i);
            if(t.id == task.id) {
              this.allTasks.splice(i, 1);
              return false;
            }
            return true;
          });
          this.isSavingTask = false;
          this.utilService.hideSpinner();
        }, 3000);
        console.log("allTask:", this.allTasks);
      }, (err) => {
        this.modal.name = "msgModal";
        this.modal.body = {
          "status": "fail",
          "data": "Unable to delete Task. Please try again later...!!"
        };
        this.isSavingTask = false;
        this.utilService.hideSpinner();
        this.showModal(this.modal);
      });
  }

  startNewTask() {
    this.currTask = new Task();
    this.currTask.owner = this.utilService.getLoggedInUser();
    this.currTask.team = new Team();
    this.currTask.category = new Stage();
    this.currTask.taskStageChanges = [];
    this.isNewTask = true;
    this.modal.name = "editTask";
    this.showModal(this.modal);
  }

  saveNewTask() {
    this.utilService.showSpinner();
    console.log(this.currTask);
    console.log("new Task", new Task(this.currTask));
    this.isSavingTask = true;
    this.hideModal();
    let task:Task = new Task(this.currTask);
    this.taskService.createNewTask(task).subscribe(
      (task:Task) => {
        setTimeout(() => {
          this.assignTaskStageChange(task);
          this.allTasks.push(task);
          this.isSavingTask = false;
          this.isNewTask = false;
          this.utilService.hideSpinner();
        }, 3000);
        console.log("allTask:", this.allTasks);
      }, (err) => {
        this.modal.name = "msgModal";
        this.modal.body = {
          "status": "fail",
          "data": "Unable to create Task. Please try again later...!!"
        };
        this.isSavingTask = false;
        this.isNewTask = false;
        this.utilService.hideSpinner();
        this.showModal(this.modal);
      });
  }

}
