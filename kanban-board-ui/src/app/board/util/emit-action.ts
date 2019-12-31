export class EmitAction {
    action:string;
    isSuccess:boolean;
    constructor(action:string, isSuccess:boolean) {
      this.action = action;
      this.isSuccess = isSuccess;
    }
  }