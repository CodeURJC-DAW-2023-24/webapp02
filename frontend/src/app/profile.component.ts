import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './services/login.service';
import { profileService } from './services/profile.service';
import { UsersService } from './services/user.service';


@Component({
  selector: 'profile',
  templateUrl: './Htmls/W-Profile.component.html',
  styleUrl: './Css/S-Main.css'
})
export class ProfileComponent {
  constructor(private loginService: LoginService, private router: Router, private profileService: profileService,
    private userService: UsersService){ }

  profileWindow(){
    
    //Checking that the user is logged
    if (this.loginService.isLogged()){

    } else {
      this.router.navigate(['LogIn'])
    }

  }
  
}
