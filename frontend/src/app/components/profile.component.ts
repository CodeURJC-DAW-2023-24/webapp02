import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { LoginService } from '../services/login.service';
import { UsersService } from '../services/user.service';
import { ElementsService } from '../services/element.service';

@Component({
  selector: 'profile',
  templateUrl: '../Htmls/W-Profile.component.html',
  styleUrls: ['../Css/S-Profile.css', '../Css/S-Main.css']
})
export class ProfileComponent {
  @Output() dataLoaded: EventEmitter<void> = new EventEmitter<void>();

  name = '';
  elementsImages: { [key: string]: string } = {};
  titleOfLists: string[] = [];
  index: number = 0;
  elementsOfUser: any[] = [];
  @Output() allElements: Element[] = [];
  elementList: Element[] | undefined = [];
  elementsOfUser2: Map<string, number[]>  = new Map;
  elementsOfUser3: Map<string, number[]>  = new Map;
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
    this.profileWindow().then(() => {
      this.dataLoaded.emit();
    });
  }

  async profileWindow(): Promise<void>{
    //Checking that the user is logged
    this.actualUser = this.loginService.currentUser();
    this.elementsOfUser2 = this.actualUser!.listasDeElementos;

    for(let [key, value] of Object.entries(this.elementsOfUser2)){
      console.log("KEY: "+ key + " VALUE: " + value);
      this.elementsOfUser3.set(key, value);
    }
    console.log("ELEMENTSOFUSER3==> " + this.elementsOfUser3)

    for (let [key, value] of this.elementsOfUser3) {
      console.log(key, value);
      for (let idX of value) {
        if (idX !== undefined) {
          this.elementService.getElementById(idX).subscribe((element: Element) => {
            if (element) {
              this.allElements.push(element);
                  if (!this.newMap.has(key)){
                    // console.log(element);
                    // if(this.allElements.includes(element)){
                    //   this.allElements.push(element);
                    // }
                    this.elementList?.push(element);
                    this.allElements.push(element);
                    this.newMap.set(key, this.elementList!);
                  } else {
                    this.elementList = [];
                    this.elementList = this.newMap.get(key);
                    this.elementList!.push(element);
                    this.newMap.set(key, this.elementList!);
                    this.elementList = [];
                  }
              this.elementService.getElementImage(idX).subscribe((imageData2) => {
                if (imageData2) {
                  const blob = new Blob([imageData2], { type: 'image/jpeg' });
                  this.elementsImages[element.name] = URL.createObjectURL(blob);
                  //this.elementsImages[this.elementService.getElementById(idX).name] = URL.createObjectURL(blob)
                  // } else { this.elementsImages[this.elementService.getElementById(idX).name] = ''}

                } else {
                  this.elementsImages[element.name] = '';
                }
              });
            } else { //element not found
              console.log("Elemento no encontrado");
            }
          });
          //this.exampleElement = this.elementService.getElementById(idX);

        } //if idx not undefined

      }//for let idx of value

    }    //for let key and value 

  }//profile window

}//Export Class


