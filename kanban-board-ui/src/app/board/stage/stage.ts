export class Stage {
    public id:string;
    public name:string;
    public projectId:string;

    constructor(obj?:any) {
        if(obj) {
            this.id = obj.id;
            this.name = obj.name;
            this.projectId = this.projectId;
        }
    }
}
