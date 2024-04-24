import { Component } from '@angular/core';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';


@Component({
  selector: 'login',
  templateUrl: '../Htmls/W-LogIn.component.html',
  styleUrl: '../Css/S-LogIn.css'
})
export class LoginComponent {
  constructor(private loginService: LoginService, private router: Router){}

  handleLoginResult(isLoggedIn: boolean) {
    if (isLoggedIn) {
      this.router.navigate(['Main']);
    }
  }
  //calls the loginService to send the name and the password
  submitForm(name: string, password: string){
    this.loginService.logIn(name, password, this.handleLoginResult.bind(this));
  }
}
