
export class Project {
  public id:string;
  public name:string;
  public description:string;
  public ownerId:string;
  public startDate:string;
  public endDate:string;

  constructor(obj?:any) {
    if(obj) {
      this.id = obj.id;
      this.name = obj.name;
      this.description = obj.description;
      this.ownerId = obj.ownerId;
      this.startDate = obj.startDate;
      this.endDate = obj.endDate;
    }
  }

}
