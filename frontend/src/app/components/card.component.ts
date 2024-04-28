import { Component, Input } from '@angular/core';
import { Element } from '../models/element.model';

@Component({
  selector: 'app-card',
  templateUrl: '../Htmls/W-Card.component.html',
  styleUrl: '../Css/S-Card.css'
})
export class CardsComponent {
  @Input() element: Element | undefined;
  @Input() image: String | undefined;
}
