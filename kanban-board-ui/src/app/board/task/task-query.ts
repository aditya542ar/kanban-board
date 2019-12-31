export class TaskQuery {
    id:string;
    idIn:Array<string>;
    name:string;
    nameIn:Array<string>;
    nameLike:string;
    categoryId:string;
    categoryIdIn:Array<string>;
    teamId:string;
    teamIdIn:Array<string>;
    userId:string;
    projectId:string;
    sortBy:string;
    sortOrder:string;

    getIdIn():Array<string> {
        if(!this.idIn) {
            this.idIn = new Array<string>();
        }
        return this.idIn;
    }

    getNameIn():Array<string> {
        if(!this.nameIn) {
            this.nameIn = new Array<string>();
        }
        return this.nameIn;
    }

    getCategoryIdIn():Array<string> {
        if(!this.categoryIdIn) {
            this.categoryIdIn = new Array<string>();
        }
        return this.categoryIdIn;
    }

    getTeamIdIn():Array<string> {
        if(!this.teamIdIn) {
            this.teamIdIn = new Array<string>();
        }
        return this.teamIdIn;
    }
}
