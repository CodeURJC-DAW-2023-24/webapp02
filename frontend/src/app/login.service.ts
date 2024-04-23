import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './models/user.model';

const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

  logged: boolean = false;
  user: User | undefined;

  constructor(private http: HttpClient) {
  }

  reqIsLogged() {

    this.http.get('/api/users/me', { withCredentials: true }).subscribe({
      next: (response) => {
        this.user = response as User;
        this.logged = true;
      },
      error: (err) => {
        if (err.status != 404) {
          console.error('Error when asking if logged: ' + JSON.stringify(err));
        }
      }
    });

  }

  //the callback helps to know
  logIn(user: string, pass: string, callback: (isLoggedIn: boolean) => void) {

    this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
      .subscribe({
        next: (response) => {
          this.reqIsLogged();
          callback(true);
        },
        error: (err) => {
          alert("Wrong credentials");
          this.logged = false;
          callback(false);
        }
      });
  }

  logOut() {

    return this.http.post(BASE_URL + '/logout', { withCredentials: true })
      .subscribe((resp: any) => {
        console.log("LOGOUT: Successfully");
        this.logged = false;
        this.user = undefined;
      });

  }

  isLogged(): Promise<boolean>{
    return new Promise<boolean>((resolve, reject) => {
      // Verificar si ya se ha iniciado sesión
      if (this.logged) {
        resolve(true);
      }else{
        this.reqIsLogged();
        const interval = setInterval(() => {
          if (this.logged) {
            clearInterval(interval);
            resolve(true);
          }
        }, 100);
      }
    });
  }

  isAdmin() {
    return this.user && this.user.roles.indexOf('ADMIN') !== -1;
  }

  currentUser() {
    return this.user;
  }
}
