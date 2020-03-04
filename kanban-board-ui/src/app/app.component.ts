import { Component } from '@angular/core';
import { UtilService } from './util/util.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'kanban-board-ui';

  constructor(public _util:UtilService) {}
}
