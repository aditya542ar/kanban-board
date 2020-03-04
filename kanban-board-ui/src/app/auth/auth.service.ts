import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../board/user/user';
import { ConfigService } from '../config/config.service';
import { UserQuery } from '../board/user/user-query';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseApiUrl = "http:localhost:8080";
  private authBaseUrl = "/authenticate";
  private revalidateBaseUrl = "/revalidate";
  private userBaseUrl = "/user";

  private readonly USER_ID:string = "userId";
  private readonly JWT_TOKEN:string = "jwtToken";
  
  constructor(private httpClient:HttpClient, private configService:ConfigService) {
    this.baseApiUrl = this.configService.getConfiguration().webApiBaseUrl;
    this.authBaseUrl = this.configService.getConfiguration().authBaseUrl;
    this.userBaseUrl = configService.getConfiguration().userBaseUrl;
  }

  registerUser(user:User) {
    user.password = btoa(user.password);
    return this.httpClient.post(this.baseApiUrl + this.userBaseUrl + "/create", user);
  }

  authenticateUser(userName:string, password:string) {
    let req = {"userName": userName, "password": btoa(password)};
    return this.httpClient.post(this.baseApiUrl + this.authBaseUrl, req);
  }

  updateUser(user:User) {
    user.password = (!!user.password) ? btoa(user.password) : undefined;
    return this.httpClient.put(this.baseApiUrl + this.userBaseUrl + "/" + user.id + "/update", user);
  }

  revalidateUser(token:string) {
    return this.httpClient.post(this.baseApiUrl + this.revalidateBaseUrl, token);
  }

  isUserLoggedIn() {
    let token = sessionStorage.getItem(this.JWT_TOKEN);
    return !!token;
  }

  getLoggedInUserId():string {
    return sessionStorage.getItem(this.USER_ID);
  }

  getToken():string {
    return sessionStorage.getItem(this.JWT_TOKEN);
  }

  fetchUserByUserId(userId?:string) {
    if(!userId) userId = this.getLoggedInUserId();
    let q:UserQuery = new UserQuery();
    q.userId = userId;
    return this.httpClient.post(this.baseApiUrl + this.userBaseUrl, q);
  }

  doLogin(username:string, jwtToken:string) {
    sessionStorage.setItem(this.USER_ID, username);
    let token = "Bearer " + jwtToken;
    sessionStorage.setItem(this.JWT_TOKEN, token);
  }

  doLogout() {
    sessionStorage.removeItem(this.USER_ID);
    sessionStorage.removeItem(this.JWT_TOKEN);
  }

}
