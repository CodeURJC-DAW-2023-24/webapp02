import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ElementsService } from '../services/element.service';
import { Element } from '../models/element.model';
import { UsersService } from '../services/user.service';
import { User } from '../models/user.model';

@Component({
  selector: 'app-singleElement',
  templateUrl: '../Htmls/W-SingleElement.component.html',
  styleUrl: '../Css/S-SingleElement.css'
})

export class SingleElementComponent {
  

  elementId: number = 0;
  element: Element | undefined;
  user: User | null = null;
  userListOfElemens: Map<string, number[]> = new Map<string, number[]>();
  selectedLista: string = '';

  constructor(private route: ActivatedRoute, private elementsService: ElementsService, private usersService: UsersService) { }

  ngOnInit(): void {
    this.elementId = Number(this.route.snapshot.paramMap.get('id'));
    this.getElementById();
    this.getCurrentUser();
  }

  getElementById(): void {
    this.elementsService.getElementById(this.elementId).subscribe({
      next: (element: Element) => {
        this.element = element;
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  getCurrentUser(): void {
    this.usersService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.user = user;
        this.userListOfElemens = user.listasDeElementos;
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }

  agregarALista($event: any) {
    $event.preventDefault(); 
    const listaDeIds = this.userListOfElemens.get(this.selectedLista);

    if (listaDeIds) {
      if (!listaDeIds.includes(this.elementId)) {
        listaDeIds.push(this.elementId);
        this.userListOfElemens.set(this.selectedLista, listaDeIds);
      } else {
        console.log('El elemento ya está en la lista.');
      }
    } else {
      console.error('La clave seleccionada no existe.');
    }
  }

}