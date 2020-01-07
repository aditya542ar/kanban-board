import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { ProjectListComponent, ProjectSearchPipe } from './board/project/project-list.component';
import { ConfigService } from './config/config.service';
import { ConfigLoader } from './config.loader';
import { ProjectComponent } from './board/project/project.component';
import { TaskDashboardComponent, TaskSearchPipe, TaskSortPipe } from './board/task/task-dashboard.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { LoginComponent } from './auth/login/login.component';
import { BasicAuthHttpInterceptorService } from './auth/basic-auth-http-interceptor.service';
import { AuthService } from './auth/auth.service';
import { RegisterComponent } from './auth/register/register.component';
import { TeamComponent } from './board/team/team.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ProjectListComponent,
    ProjectComponent,
    ProjectSearchPipe,
    TaskDashboardComponent,
    TaskSearchPipe,
    TaskSortPipe,
    LoginComponent,
    RegisterComponent,
    TeamComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    FontAwesomeModule
  ],
  providers: [
    ConfigService,
    AuthService,
    {
      provide: APP_INITIALIZER,
      useFactory: ConfigLoader,
      deps: [ConfigService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS, 
      useClass: BasicAuthHttpInterceptorService, 
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
