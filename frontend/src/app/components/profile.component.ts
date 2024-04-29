import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { LoginService } from '../services/login.service';
import { UsersService } from '../services/user.service';
import { ElementsService } from '../services/element.service';
import { forkJoin } from 'rxjs';

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

  allElementsPromise: Promise<Element[]> | undefined;

  constructor(private loginService: LoginService, private router: Router,
    private userService: UsersService, private elementService: ElementsService) { }

  // ngOnInit() {
  //   this.profileWindow();
  // }
  // ngOnInit() {
  //   this.allElementsPromise = this.fetchAllElements();
  // }

  // async fetchAllElements(): Promise<Element[]> {
  //   const allElements: Element[] = [];
  //   this.profileWindow();
  //   return allElements;
  // }

  ngOnInit() {
    this.profileWindow();
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
              this.allElements.push(element);
              if (!this.newMap.has(key)) {
                this.elementList?.push(element);
                //this.allElements.push(element);
                this.newMap.set(key, this.elementList!);
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
              //this.dataLoaded.emit(this.allElements);
            } else { //element not found
              console.log("Elemento no encontrado");
            }
          });
          this.dataLoaded.emit(this.allElements); //al mandarlo desde aquí solo pilla la 1ªlista
        });
        //this.dataLoaded.emit(this.allElements);
      }
    }

  }//profile window

  recieveUser(userRecieved: User) {
    this.actualUser = userRecieved;
  }

}//Export Class



