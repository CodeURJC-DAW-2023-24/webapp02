import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { User } from '../models/user.model';

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

	addOrUpdateUser(User: User) {
		if (!User.id) {
			return this.addUser(User);
		} else {
			return this.updateUser(User);
		}
	}

	private addUser(User: User) {
		return this.httpClient.post(BASE_URL, User).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateUser(User: User) {
		return this.httpClient.put(BASE_URL + User.id, User).pipe(
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
