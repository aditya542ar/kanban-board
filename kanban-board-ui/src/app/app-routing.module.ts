import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProjectListComponent } from './board/project/project-list.component';
import { ProjectComponent } from './board/project/project.component';

const routes: Routes = [
  {path: '' , pathMatch: "full", redirectTo: "projectList"},
  {path: 'projectList' , pathMatch: "full", component: ProjectListComponent},
  {path: 'project/:id' , component: ProjectComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
