import { Component, Input } from '@angular/core';
import { Element } from '../models/element.model';

@Component({
  selector: 'app-tops',
  templateUrl: '../Htmls/W-Top.component.html',
  styleUrl: '../Css/S-Top.css'
})
export class TopsComponent {
  @Input() topBooks: Element[] = [];
}