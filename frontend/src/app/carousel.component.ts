import { Component, Input } from '@angular/core';
import { Element } from './models/element.model';

@Component({
  selector: 'carousel-comp',
  templateUrl: 'Htmls/W-Carousel.component.html',
  styleUrl: 'Css/S-Carousel.component.css'
})
export class CarouselComponent {

  @Input() elements: Element[] = [];
}
