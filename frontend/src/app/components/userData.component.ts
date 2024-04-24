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
  base64image:string | undefined;
  isAdmin: boolean | undefined;

  constructor(private loginService: LoginService, private userService: UsersService, private router: Router){}

  async ngOnInit(){
    this.isLogged = await this.loginService.isLogged();

    if (this.isLogged){
      this.currentUser = this.loginService.currentUser();
      this.isLogged = true;
      if(this.currentUser && this.currentUser.id !== undefined){
        //this.userService.getUserImage(this.currentUser?.id);
        this.isAdmin = this.loginService.isAdmin();
        this.userService.getUserImage(this.currentUser?.id).subscribe((imageData: ArrayBuffer) => {
          this.base64image = this.arrayBufferToBase64(imageData);
        });
      }
    }
  }
  arrayBufferToBase64(imageData: ArrayBuffer): string {
    let binary = '';
    const bytes = new Uint8Array(imageData);
    const len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return 'data:image/jpeg;base64,' + btoa(binary);
  }

  logout(){
    this.loginService.logOut();
  }


}
