import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from './models/user.model';
import {Element} from './models/element.model';
import { LoginService } from './services/login.service';
import { profileService } from './services/profile.service';
import { UsersService } from './services/user.service';

@Component({
  selector: 'profile',
  templateUrl: './Htmls/W-Profile.component.html',
  styleUrl: './Css/S-Main.css'
})
export class ProfileComponent {
  name = '';
  elementsOfUser: Element [] = [];
  actualUser: User | undefined;

  constructor(private loginService: LoginService, private router: Router, private profileService: profileService,
    private userService: UsersService){ }

  profileWindow(){
    
    //Checking that the user is logged
    if (this.loginService.isLogged()){
      this.actualUser = this.loginService.currentUser();
      
      this.elementsOfUser = this.actualUser?.elements;

    } else {
      this.router.navigate(['LogIn'])
    }

  }
  
}
