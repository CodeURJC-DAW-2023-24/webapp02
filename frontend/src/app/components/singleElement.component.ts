import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ElementsService } from '../services/element.service';
import { Element } from '../models/element.model';
import { UsersService } from '../services/user.service';
import { User } from '../models/user.model';
import { UserDTO } from '../models/userDTO.model';

@Component({
  selector: 'app-singleElement',
  templateUrl: '../Htmls/W-SingleElement.component.html',
  styleUrl: '../Css/S-SingleElement.css'
})

export class SingleElementComponent {
  

  elementId: number = 0;
  element: Element | undefined;
  userDTO: UserDTO | null = null;
  user: User | null = null;
  userListOfElemens: Map<string, number[]>;
  selectedLista: string = '';

  constructor(private route: ActivatedRoute, private elementsService: ElementsService, private usersService: UsersService) { this.userListOfElemens = new Map<string, number[]>();}

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
        this.userListOfElemens = this.convertObjectToMap(user.listasDeElementos);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }

  convertObjectToMap(obj: any): Map<string, number[]> {
    const map = new Map<string, number[]>();
    for (const key in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, key)) {
            map.set(key, obj[key]);
        }
    }
    return map;
}

  agregarALista($event: any) {
    $event.preventDefault(); 
    var l: Map<string, number[]> = new Map<string, number[]>();
    l = this.userListOfElemens;
    const d = this.selectedLista;
    const listaDeIds = l.get(d);

    if (listaDeIds) {
      if (!listaDeIds.includes(this.elementId)) {
        listaDeIds.push(this.elementId);
        this.userListOfElemens.set(this.selectedLista, listaDeIds);
        if (this.user!=null){
          this.userDTO = {
            name:null,
            roles:null,
            password:null,
            listasDeElementos: this.userListOfElemens
          }
          this.usersService.addOrUpdateUser(this.userDTO, this.user).subscribe({
            next: () => {
              console.log("HDLRCMQTP POR FAVOR FUNCIONA")
            },
            error: (error) => {
              console.error('Error:', error);
            }
          });
        }
      } else {
        console.log('El elemento ya está en la lista.');
      }
    } else {
      console.error('La clave seleccionada no existe.');
    }
  }

}