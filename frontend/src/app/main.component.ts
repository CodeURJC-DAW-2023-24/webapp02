import { Component } from '@angular/core';
import { ElementsService } from './element.service';
import { Element } from './models/element.model';


@Component({
  selector: 'main',
  templateUrl: './Htmls/W-Main.component.html',
  styleUrl: './Css/S-Main.css'
})
export class MainComponent {

  elements: Element[] = [];

  constructor(private elementsService: ElementsService) { }

  ngOnInit(): void {
    this.getAllElements();
  }

  getAllElements(): void {
    this.elementsService.getAllElements().subscribe({
      next: (response: Element[]) => {
        this.elements = response;
        // Otros procesamientos de la respuesta, si es necesario
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  


}
