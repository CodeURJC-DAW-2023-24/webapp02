import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { UsersService } from "../services/user.service";

@Component({
  selector: 'signIn',
  templateUrl: '../Htmls/W-SignIn.component.html',
  styleUrl: '../Css/S-LogIn.css'
})


export class signInComponent{
  userName: string = "";
  password: string = "";
  password1: string = "";

  constructor(private router: Router, private userService: UsersService) {
  }

  signIn($event: any){
    $event.preventDefault();
    if(this.password != this.password1){

    }else{

    }
  }
}
