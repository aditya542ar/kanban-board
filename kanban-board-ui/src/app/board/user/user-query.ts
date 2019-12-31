export class UserQuery {
    id:string;
    idIn:Array<string>;
    firstName:string;
    firstNameLike:string;
    lastName:string;
    lastNameLike:string;
    userId:string;
    userIdIn:Array<string>;
    userIdLike:string;
    sortBy:string;
    sortData:string;

    getIdIn():Array<string> {
        if(!this.idIn) {
            this.idIn = new Array<string>();
        }
        return this.idIn;
    }

    getUserIdIn():Array<string> {
        if(!this.userIdIn) {
            this.userIdIn = new Array<string>();
        }
        return this.userIdIn;
    }
}
