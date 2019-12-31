export class Team {
    public id:string;
    public name:string;
    public projectId:string;

    constructor(obj?:any) {
        if(obj) {
            this.id = obj.id;
            this.name = obj.name;
            this.projectId = obj.projectId;
        }
    }

}
