import { Component } from '@angular/core';
import { LoginService } from '../services/login.service';
import { User } from '../models/user.model';
import { UsersService } from '../services/user.service';
import { Router } from '@angular/router';


@Component({
  selector: 'userData',
  templateUrl: '../Htmls/W-UserData.component.html',
  styleUrl: '../Css/S-LogIn.css'
})
export class UserDataComponent {

  currentUser: User | undefined;
  private name:String | undefined;
  isLogged: boolean | undefined;
  userImage:string | undefined;
  isAdmin: boolean | undefined;

  constructor(private loginService: LoginService, private userService: UsersService, private router: Router){}

  ngOnInit(){
    this.isLogged = this.loginService.isLogged();

    if (this.isLogged){
      if(this.currentUser == undefined){
        this.currentUser = this.loginService.currentUser();
      }

      this.isLogged = true;
      if(this.currentUser && this.currentUser.id !== undefined){
        this.isAdmin = this.loginService.isAdmin();
        this.userService.getUserImage(this.currentUser?.id).subscribe((imageData) => {
          if(imageData){
            const blob = new Blob([imageData], {type: 'image/jpeg'});
            this.userImage = URL.createObjectURL(blob);
            this.currentUser!.imageURL = URL.createObjectURL(blob);
            localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
          }else{
            this.userImage= undefined;
          }

        });
      }

      //observer to update the current user when it detects any changes
      this.loginService.user$.subscribe(user => {
        if(user != undefined){
          this.currentUser = user;
        }
      });

      localStorage.setItem('currentUser', JSON.stringify(this.currentUser));

    }
  }

  logout(){
    this.loginService.logOut();
  }


}
