import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { User } from '../models/user.model';
import { UserDTO } from '../models/userDTO.model';

const BASE_Url = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UsersService {


	constructor(private httpClient: HttpClient) { }

	getUsers(): Observable<User[]> {
		return this.httpClient.get(BASE_Url).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<User[]>;
	}

	getUser(id: number | string): Observable<User> {
		return this.httpClient.get(BASE_Url + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<User>;
	}

	getUserImage(id: number | string) {
		return this.httpClient.get(BASE_Url + id + '/image', { responseType: 'arraybuffer' })
	}

	addOrUpdateUser(userDTO: UserDTO, user: User) {
		if (!user.id) {
			return this.addUser(user);
		} else {
			return this.updateUser(userDTO, user);
		}
	}

	private addUser(user: User) {
		const u: any = {
			name: user.name,
			password: user.password,
			roles: user.roles,
			listasDeElementos: {"Favoritos": []}
		};
		return this.httpClient.post(BASE_Url, u).pipe(
			tap((response: any) => {
				this.updateUserImage(user, response.id).subscribe({
					next: (response) => {
						console.log('Solicitud POST de imagen completada con éxito:', response);
					},
					error: (error) => {
						console.error('Error al actualizar la imagen del usuario:', error);
					}
				});
				console.log('Solicitud POST completada con éxito:', response);
			}),
			catchError(error => this.handleError(error))
		);

	}

	private updateUserImage(user: User, id: number) {
		const formData = new FormData();
		formData.append('imageUrl', user.imageURL!);
		return this.httpClient.post(BASE_Url + id + '/image', formData).pipe(
			tap((response) => {
				console.log('Solicitud POST de imagen completada con éxito:', response);
			}),
			catchError(error => this.handleError(error))
		);
	}

	private updateUser(userDTO: UserDTO, user: User) {
		const u: any = {
			listasDeElementos: {}
		};
		const x = userDTO.listasDeElementos;
		x?.forEach((value: number[], key: string) => {
			u.listasDeElementos[key] = value;
		});

		return this.httpClient.put<User>(BASE_Url + user.id, u).pipe(
      tap((response) => {
				localStorage.setItem('currentUser', JSON.stringify(response));
			}),
			catchError(error => this.handleError(error))
		);
	}

	removeUser(User: User) {
		return this.httpClient.delete(BASE_Url + User.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}

  updateCurrentUser(user: User){
    localStorage.setItem('currentUser', JSON.stringify(user));
  }
}
