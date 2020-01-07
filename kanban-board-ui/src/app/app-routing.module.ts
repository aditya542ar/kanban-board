import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProjectListComponent } from './board/project/project-list.component';
import { ProjectComponent } from './board/project/project.component';
import { TaskDashboardComponent } from './board/task/task-dashboard.component';
import { LoginComponent } from './auth/login/login.component';
import { AuthGaurdService } from './auth/auth-gaurd.service';
import { RegisterComponent } from './auth/register/register.component';
import { TeamComponent } from './board/team/team.component';

const routes: Routes = [
  {path: '' , pathMatch: "full", redirectTo: "project-list", canActivate: [AuthGaurdService]},
  {path: 'login' , pathMatch: "full", component: LoginComponent},
  {path: 'register' , pathMatch: "full", component: RegisterComponent},
  {path: 'project-list' , pathMatch: "full", component: ProjectListComponent, canActivate: [AuthGaurdService]},
  {path: 'project/:id' , component: ProjectComponent, canActivate: [AuthGaurdService]},
  {path: 'project/:id/user-list' , component: TeamComponent, canActivate: [AuthGaurdService]},
  {path: 'dashboard' , pathMatch: 'full', component: TaskDashboardComponent, canActivate:[AuthGaurdService]},
  {path: 'dashboard/:projectId' , component: TaskDashboardComponent, canActivate:[AuthGaurdService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
