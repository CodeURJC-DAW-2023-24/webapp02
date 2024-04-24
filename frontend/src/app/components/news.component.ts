import { Component, Input } from '@angular/core';
import { New } from '../models/new.model';


@Component({
  selector: 'app-news',
  templateUrl: '../Htmls/W-News.component.html',
  styleUrl: '../Css/S-News.css'
})
export class NewsComponent {

  @Input() news: New[] = [];
}
