import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ElementsService } from '../services/element.service';
import { Element as ElementComponent} from '../models/element.model';
import { UsersService } from '../services/user.service';
import { LoginService } from '../services/login.service';
import { User } from '../models/user.model';
import { UserDTO } from '../models/userDTO.model';

@Component({
  selector: 'app-singleElement',
  templateUrl: '../Htmls/W-SingleElement.component.html',
  styleUrls: ['../Css/S-Main.css','../Css/S-SingleElement.css']
})

export class SingleElementComponent {
  

  elementId: number = 0;
  element!: ElementComponent;
  elementImage: string = '';
  userDTO: UserDTO | null = null;
  user: User | undefined;
  userListOfElemens: Map<string, number[]> = new Map<string, number[]>();
  selectedList: string = '';

  constructor(private route: ActivatedRoute, private elementsService: ElementsService, private loginService: LoginService, private usersService: UsersService) {}

  ngOnInit(): void {
    this.elementId = Number(this.route.snapshot.paramMap.get('id'));
    this.getElementById();
    this.getCurrentUser();
  }

  getElementById(): void {
    this.elementsService.getElementById(this.elementId).subscribe({
      next: (element: ElementComponent) => {
        this.element = element;
        if (element.id !== undefined) {
          this.elementsService.getElementImage(element.id).subscribe((imageData) => {
            if (imageData) {
              const blob = new Blob([imageData], { type: 'image/jpeg' });
              this.elementImage = URL.createObjectURL(blob)
            } else {
              this.elementImage = ''
            }
          });
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  getCurrentUser(): void {
    this.user = this.loginService.currentUser();
    if(this.user!= undefined){
      this.userListOfElemens = this.convertObjectToMap(this.user.listasDeElementos);
    }
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

  addToList($event: any) {
    $event.preventDefault(); 
    var l: Map<string, number[]> = new Map<string, number[]>();
    l = this.userListOfElemens;
    const d = this.selectedList;
    const listaDeIds = l.get(d);

    if (listaDeIds) {
      if (!listaDeIds.includes(this.elementId)) {
        listaDeIds.push(this.elementId);
        this.userListOfElemens.set(this.selectedList, listaDeIds);
        if (this.user!=null){
          this.userDTO = {
            name:null,
            roles:null,
            password:null,
            listasDeElementos: this.userListOfElemens
          }
          this.usersService.addOrUpdateUser(this.userDTO, this.user).subscribe({
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