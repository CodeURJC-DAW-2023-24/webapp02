import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { User } from '../models/user.model';
import { UserDTO } from '../models/userDTO.model';

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UsersService {


	constructor(private httpClient: HttpClient) { }

	getUsers(): Observable<User[]> {
		return this.httpClient.get(BASE_URL).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<User[]>;
	}

	getUser(id: number | string): Observable<User> {
		return this.httpClient.get(BASE_URL + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<User>;
	}

	getCurrentUser(): Observable<User> {
		return this.httpClient.get<User>(BASE_URL + "me")
			.pipe(
				catchError((error: HttpErrorResponse) => {
					console.error('Error:', error);
					return throwError(() => new Error('Server error: ' + error.statusText));
				})
			);
	}
	getUserImage(id: number | string) {
		return this.httpClient.get(BASE_URL + id + '/image', { responseType: 'arraybuffer' })
	}

	addOrUpdateUser(userDTO: UserDTO, user: User) {
		if (!user.id) {
			return this.addUser(user);
		} else {
			return this.updateUser(userDTO, user);
		}
	}

	private addUser(user: User) {
		return this.httpClient.post(BASE_URL, user).pipe(
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
		
		return this.httpClient.put<User>(BASE_URL + user.id, u).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeUser(User: User) {
		return this.httpClient.delete(BASE_URL + User.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}
