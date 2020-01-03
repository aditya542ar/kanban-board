import { Stage } from '../stage/stage';
import { Team } from '../team/team';
import { User } from '../user/user';

class TaskStageChange {
    public id:string;
    public stage:Stage;
    public startTime:string;
    public endTime:string;
    public version:number;
}

export class Task {
    public id:string;
    public name:string;
    public description:string;
    public priority:number;
    public category:Stage;
    public team:Team;
    public owner:User;
    public taskStageChanges:Array<TaskStageChange> = new Array<TaskStageChange>();

    constructor(obj?:any) {
        if(obj){
            this.id = obj.id;
            this.name = obj.name;
            this.description = obj.description;
            this.priority = obj.priority;
            this.category = new Stage(obj.category);
            this.team = new Team(obj.team);
            this.owner = new User(obj.owner);
            this.taskStageChanges = obj.taskStageChanges;
        }
    }
}
