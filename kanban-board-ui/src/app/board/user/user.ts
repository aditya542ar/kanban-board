export class User {
    public id:string;
    public firstName:string;
    public lastName:string;
    public userId:string;
    public profilePic:string;

    constructor(obj?:any) {
        if(obj) {
            this.id = obj.id;
            this.firstName = obj.firstName;
            this.lastName = obj.lastName;
            this.userId = obj.userId;
            this.profilePic = obj.profilePic;
        }
    }
}
