import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import {Element} from '../models/element.model';
import { LoginService } from '../services/login.service';
import { UsersService } from '../services/user.service';
import { ElementsService } from '../services/element.service';

@Component({
  selector: 'profile',
  templateUrl: '../Htmls/W-Profile.component.html',
  styleUrls: ['../Css/S-Profile.css','../Css/S-Main.css']
})
export class ProfileComponent {
  name = '';
  elementsImages: { [key: string]: string } = {};
  elementsOfUser:any[]= [];
  actualUser: User | undefined;

  constructor(private loginService: LoginService, private router: Router,
    private userService: UsersService, private elementService: ElementsService){ }

  ngOnInit(){
    this.profileWindow();
  }

  profileWindow(){
    //Checking that the user is logged
      this.actualUser = this.loginService.currentUser();
      

      //TO-DO make a for in thisActualUser.elements to get all elements 1 by 1

      this.elementsOfUser.push(this.actualUser?.elements);
      for (let elementX of this.elementsOfUser[0]){
        //for(let elementX of elements){
          if(elementX.id !== undefined){
            this.elementService.getElementImage(elementX.id).subscribe((imageData) => {
              if (imageData){
                const blob = new Blob([imageData], {type: 'image/jpeg'});
                this.elementsImages[elementX.name] = URL.createObjectURL(blob)
              } else {
                this.elementsImages[elementX.name] = ''
              }
            });
          }
        //}
        
      }

  }
  
}
