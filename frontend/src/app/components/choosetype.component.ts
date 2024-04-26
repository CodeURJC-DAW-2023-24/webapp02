// otro-componente.component.ts

import { Component, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'admin',
    templateUrl: '../Htmls/W-ChooseElementTypePage.component.html',
    styleUrls: ['../Css/S-Main.css', "../Css/S-Admin.css", "../Css/S-NavBar.css"]
  })
export class ChooseTypeComponent {
  @Input() elementType: string[] = []; // Propiedad para recibir el tipo de elemento

  constructor() { }

  // Método que se ejecuta cuando se inicializa el componente
  ngOnInit(types : string[]): void {
    console.log(`Tipo de elemento recibido: ${this.elementType}`);
    // Aquí puedes realizar otras acciones según el tipo de elemento
  }
}
