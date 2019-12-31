import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { ProjectListComponent, ProjectSearchPipe } from './board/project/project-list.component';
import { ConfigService } from './config/config.service';
import { ConfigLoader } from './config.loader';
import { ProjectComponent } from './board/project/project.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ProjectListComponent,
    ProjectComponent,
    ProjectSearchPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    ConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: ConfigLoader,
      deps: [ConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
