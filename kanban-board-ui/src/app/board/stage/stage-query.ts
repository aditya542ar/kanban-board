export class StageQuery {
    id:string;
    idIn:Array<string>;
    name:string;
    nameIn:Array<string>;
    nameLike:string;
    projectId:string;
    projectIdIn:Array<string>;
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

    getProjectIdIn():Array<string> {
        if(!this.projectIdIn) {
            this.projectIdIn = new Array<string>();
        }
        return this.projectIdIn;
    }
}
