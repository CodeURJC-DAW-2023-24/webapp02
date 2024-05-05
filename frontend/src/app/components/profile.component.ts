import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { LoginService } from '../services/login.service';
import { UsersService } from '../services/user.service';
import { ElementsService } from '../services/element.service';
import { forkJoin } from 'rxjs';
import { UserDTO } from '../models/userDTO.model';

@Component({
  selector: 'profile',
  templateUrl: '../Htmls/W-Profile.component.html',
  styleUrls: ['../Css/S-Profile.css', '../Css/S-Main.css']
})
export class ProfileComponent {
  @Output() dataLoaded: EventEmitter<Element[]> = new EventEmitter();

  name = '';
  elementsImages: { [key: string]: string } = {};
  titleOfLists: string[] = [];
  index: number = 0;
  elementsOfUser: any[] = [];
  @Output() allElements: Element[] = [];
  elementList: Element[] | undefined = [];
  elementsOfUser2: Map<string, number[]> = new Map;
  elementsOfUser3: Map<string, number[]> = new Map;
  newMap: Map<string, Element[]> = new Map;
  exampleElement!: Element;
  actualUser: User | undefined;

  newUserListToAdd: Map<string, number[]> = new Map;
  newNameList: string = '';
  userUpdateVar: User | undefined;

  allElementsPromise: Promise<Element[]> | undefined;

  constructor(private loginService: LoginService, private router: Router,
    private userService: UsersService, private elementService: ElementsService) { }

  ngOnInit() {
    this.profileWindow();
  }

  recieveBannerURL($event: string){
    this.actualUser!.bannerImageURL = $event;
  }

  profileWindow() {
    //Checking that the user is logged
    this.actualUser = this.loginService.currentUser();
    this.elementsOfUser2 = this.actualUser!.listasDeElementos;

    for (let [key, value] of Object.entries(this.elementsOfUser2)) {
      console.log("KEY: " + key + " VALUE: " + value);
      this.elementsOfUser3.set(key, value);
    }
    console.log("ELEMENTSOFUSER3==> " + this.elementsOfUser3)

    for (let [key, value] of this.elementsOfUser3) {
      console.log(key, value);
      const observables = [];
      for (let idX of value) {
        if (idX !== undefined) {
          const elementObs = this.elementService.getElementById(idX);
          const imageObs = this.elementService.getElementImage(idX);
          observables.push(forkJoin([elementObs, imageObs]));
        }
      }
      if (observables.length > 0) {
        forkJoin(observables).subscribe((results: [Element, ArrayBuffer][]) => {
          results.forEach(([element, imageData]) => {
            if (element) {
              console.log("ELEMENTO: ", element)
              if (!this.allElements.some(el =>
                el.name === element.name &&
                el.description === element.description &&
                el.year === element.year &&
                el.type === element.type &&
                el.season === element.season &&
                el.state === element.state &&
                el.author === element.author &&
                el.country === element.country &&
                el.base64Image === element.base64Image &&
                el.generos.length === element.generos.length && 
                el.generos.every((genre, index) => genre === element.generos[index])
              )) {
                this.allElements.push(element);
              }
              if (!this.newMap.has(key)) {
                this.elementList?.push(element);
                //this.allElements.push(element);
                this.newMap.set(key, this.elementList!);
                this.elementList = [];
              } else {
                this.elementList = [];
                this.elementList = this.newMap.get(key);
                this.elementList!.push(element);
                this.newMap.set(key, this.elementList!);
                this.elementList = [];
              }
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.elementsImages[element.name] = URL.createObjectURL(blob);
              } else {
                this.elementsImages[element.name] = '';
              }
              
            } else { //element not found
              console.log("Elemento no encontrado");
            }
          });
          this.dataLoaded.emit(this.allElements); 
        });
        
      }
    }

  }//profile window
  addList(nameList: string) {
    this.newNameList = nameList;
    if (this.newNameList !== '') {
      this.elementsOfUser2 = this.actualUser!.listasDeElementos;

      for (let [key, value] of Object.entries(this.elementsOfUser2)) {
        this.elementsOfUser3.set(key, value);
      }

      this.elementsOfUser3.set(this.newNameList, []);
      this.actualUser!.listasDeElementos = this.elementsOfUser3;

      const userdto: UserDTO = {
        listasDeElementos: this.actualUser?.listasDeElementos,
        profileImageUrl: this.actualUser?.imageURL,
        bannerImageUrl: this.actualUser?.bannerImageURL
      };

      this.userService.addOrUpdateUser(userdto, this.actualUser!).subscribe({
        next: (response: any) => {
          // this.userUpdateVar = response as User;
          this.actualUser!.listasDeElementos = response.listasDeElementos;
          this.loginService.updateCurrentUser(this.actualUser);
          this.userService.updateCurrentUser(this.actualUser!);
          this.actualUser = JSON.parse(localStorage.getItem('currentUser')!) as User;
          this.actualUser!.id = response.id;
          console.log("Lista de Usuario Actualizada");
        },
        error: (error) => {
          console.error('Error:', error);
        }
      });
    }

  }

  recieveUser(userRecieved: User) {
    this.actualUser = userRecieved;
  }

}//Export Class



