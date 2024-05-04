import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { UsersService } from "../services/user.service";
import { User as UserModel} from "../models/user.model";
import { UserDTO } from "../models/userDTO.model";
import { Observable } from "rxjs";

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
      alert("Las contraseñas no coinciden");
    }else{
      const userRoles: string[] = [];
      userRoles.push('USER');

      const userList: Map<string, number[]> = new Map<string, number[]>();
      userList.set('Favoritos', []);


      const newUser: UserModel = {
        name: this.userName,
        password: this.password,
        roles:userRoles,
        listasDeElementos: userList,
      };

      const userdto: UserDTO = {};
      this.userService.addOrUpdateUser(userdto, newUser).subscribe({
        next: (response) => {
          this.router.navigate(["/Login"]);
        },
        error: (error) => {alert("No se pudo añadir al usuario");}
      });
    }
  }

}
