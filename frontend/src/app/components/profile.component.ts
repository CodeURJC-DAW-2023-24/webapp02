import { Component } from '@angular/core';
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
  name = '';
  elementsImages: { [key: string]: string } = {};
  elementsOfUser: any[] = [];
  listOfElements: Element[] = [];
  elementsOfUser2: Map<string, number[]>  = new Map;
  exampleElement!: Element;
  actualUser: User | undefined;

  constructor(private loginService: LoginService, private router: Router,
    private userService: UsersService, private elementService: ElementsService) { }

  ngOnInit() {
    this.profileWindow();
  }

  profileWindow() {
    //Checking that the user is logged
    this.actualUser = this.loginService.currentUser();


    //TO-DO make a for in thisActualUser.elements to get all elements 1 by 1
    //this.elementsOfUser.push(this.actualUser?.elements);
    //this.elementsOfUser.push(this.actualUser?.listasDeElementos);

    this.elementsOfUser2 = this.actualUser!.listasDeElementos;
    console.log(this.elementsOfUser2);
    for (let [key, value] of Array.from(this.elementsOfUser2)) {
      console.log(key, value);
      for (let idX of value) {
        if (idX !== undefined) {
          this.elementService.getElementById(idX).subscribe((element: Element) => {
            if (element) {
              this.elementService.getElementImage(idX).subscribe((imageData2) => {
                if (imageData2) {
                  const blob = new Blob([imageData2], { type: 'image/jpeg' });
                  this.elementsImages[element.name] = URL.createObjectURL(blob);
                  this.listOfElements.push(element);

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
        //ANTERIOR muestra de elements pues no se usaban las listas:
        // for (let elementX of this.elementsOfUser[0]){
        //   for(let elementX of elements){
        //     if(elementX.id !== undefined){
        //       this.elementService.getElementImage(elementX.id).subscribe((imageData) => {
        //         if (imageData){
        //           const blob = new Blob([imageData], {type: 'image/jpeg'});
        //           this.elementsImages[elementX.name] = URL.createObjectURL(blob)
        //         } else {
        //           this.elementsImages[elementX.name] = ''
        //         }
        //       });
        //     }
        //   }

        // }


