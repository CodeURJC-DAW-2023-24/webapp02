import { Component } from '@angular/core';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';


@Component({
  selector: 'library',
  templateUrl: '../Htmls/W-Library.component.html',
  styleUrls: ['../Css/S-Main.css', '../Css/S-Library.css']
})
export class LibraryComponent {
  constructor(private loginService: LoginService, private router: Router){}


  //calls the loginService to send the name and the password
  submitForm(name: string, password: string){
    this.loginService.logIn(name, password);
  }
}
