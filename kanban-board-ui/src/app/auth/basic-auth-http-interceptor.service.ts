import { Injectable, Injector } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpResponse, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthHttpInterceptorService implements HttpInterceptor{

  constructor(private injector:Injector, private router:Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log("intercept");
    let auth = this.injector.get(AuthService);
    if (auth.getLoggedInUserId() && auth.getToken()) {
      req = req.clone({
        setHeaders: {
          Authorization: auth.getToken()
        }
      });
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          if(auth.getLoggedInUserId() || auth.getToken()) {
            auth.doLogout();
            this.router.navigate(["/login"]);
          }     
        }
        return throwError(error);
      })
      );

  }
}
