import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProjectListComponent } from './board/project/project-list.component';
import { ProjectComponent } from './board/project/project.component';
import { TaskDashboardComponent } from './board/task/task-dashboard.component';

const routes: Routes = [
  {path: '' , pathMatch: "full", redirectTo: "dashboard"},
  {path: 'projectList' , pathMatch: "full", component: ProjectListComponent},
  {path: 'project/:id' , component: ProjectComponent},
  {path: 'dashboard' , pathMatch: 'full', component: TaskDashboardComponent},
  {path: 'dashboard/:projectId' , component: TaskDashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
