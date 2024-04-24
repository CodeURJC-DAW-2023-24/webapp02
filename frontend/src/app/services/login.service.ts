import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Router } from '@angular/router';

const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

  logged: boolean = false;
  user: User | undefined;

  constructor(private http: HttpClient, public router: Router) {
  }

  reqIsLogged() {

    this.http.get('/api/users/me', { withCredentials: true }).subscribe({
      next: (response) => {
        this.user = response as User;
        this.logged = true;
        this.router.navigate(['/Main']);
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error when asking if logged: ' + JSON.stringify(err));
        }
      }
    });

  }

  //the callback helps to know
  logIn(user: string, pass: string) {

    this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
      .subscribe({
        next: (response) => {
          this.reqIsLogged();
        },
        error: (err) => {
          alert("Wrong credentials");
          this.logged = false;
        }
      });
  }

  logOut() {

    return this.http.post(BASE_URL + '/logout', { withCredentials: true })
      .subscribe((resp: any) => {
        console.log("LOGOUT: Successfully");
        this.logged = false;
        this.user = undefined;
        this.router.navigate(['/Login']);
      });

  }

  isLogged(){
    return this.logged;
  }

  isAdmin() {
    return this.user && this.user.roles.indexOf('ADMIN') !== -1;
  }

  currentUser() {
    return this.user;
  }
}
