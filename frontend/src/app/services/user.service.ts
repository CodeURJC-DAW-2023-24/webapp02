import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { User } from '../models/user.model';
import { UserDTO } from '../models/userDTO.model';

const BASE_Url = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UsersService {

	user: User | undefined;

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

	getUserBannerImage(id: number | string) {
		return this.httpClient.get(BASE_Url + id + '/bannerimage', { responseType: 'arraybuffer' })
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
			listasDeElementos: { "Favoritos": [] }
		};
		return this.httpClient.post(BASE_Url, u).pipe(
			tap((response: any) => {
				this.uploadUserImage(user, response.id).subscribe({
					next: (response) => {
						console.log('Solicitud POST de imagen de perfil completada con éxito:', response);
					},
					error: (error) => {
						console.error('Error al actualizar la imagen del usuario:', error);
					}
				});

        this.uploadUserBannerImage(user, response.id).subscribe({
					next: (response) => {
						console.log('Solicitud POST de imagen baner completada con éxito:', response);
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

	private uploadUserImage(user: User, id: number) {
		const formData = new FormData();
		formData.append('imageUrl', user.imageURL!);
		return this.httpClient.post(BASE_Url + id + '/image', formData).pipe(
			tap((response) => {
				localStorage.setItem('currentUser', JSON.stringify(response));
				console.log('Solicitud POST de imagen completada con éxito:', response);
			}),
			catchError(error => this.handleError(error))
		);
	}

  private uploadUserBannerImage(user: User, id: number) {
		const formData = new FormData();
		formData.append('bannerImageURL', user.bannerImageURL!);
		return this.httpClient.post(BASE_Url + id + '/bannerImage', formData).pipe(
			tap((response) => {
				localStorage.setItem('currentUser', JSON.stringify(response));
				console.log('Solicitud POST de imagen completada con éxito:', response);
			}),
			catchError(error => this.handleError(error))
		);
	}

	private updateUser(userDTO: UserDTO, user: User) {
		//make the Json that will be sent to the api.
		const u: any = {
			name: userDTO.name,
			listasDeElementos: {}
		};
		const x = userDTO.listasDeElementos;
		x?.forEach((value: number[], key: string) => {
			u.listasDeElementos[key] = value;
		});

		return this.httpClient.put<User>(BASE_Url + user.id, u).pipe(
			tap((response) => {
				this.user = response as User;
				this.user.name = userDTO.name!;
				this.user.profileImage = userDTO.profileImage;
				this.user.bannerImage = userDTO.bannerImage;
				this.user.imageURL = userDTO.profileImageUrl;
				this.user.bannerImageURL = userDTO.bannerImageUrl;
				localStorage.setItem('currentUser', JSON.stringify(this.user));
			}),
			catchError(error => this.handleError(error))
		);
	}

	setUserImage(id: number | string, userDTO: UserDTO) {
		// return this.httpClient.put(BASE_Url + id + '/image', {responseType: 'arraybuffer'})
		const formData = new FormData();
		formData.append('profileImage', userDTO.profileImage!);

		return this.httpClient.put(BASE_Url + id + '/image', formData).pipe(
			tap((response) => {
				this.user = response as User;
				this.user.profileImage = userDTO.profileImage;
				this.user.bannerImage = userDTO.bannerImage;
				this.user.imageURL = userDTO.profileImageUrl;
				this.user.bannerImageURL = userDTO.bannerImageUrl;
				localStorage.setItem('currentUser', JSON.stringify(this.user));
				console.log('Solicitud PUT de imagen completada con éxito:', response);
			}),
			catchError(error => this.handleError(error))
		);
	}

	setUserBannerImage(id: number | string, userDTO: UserDTO) {
		const formData = new FormData();
		formData.append('bannerImage', userDTO.bannerImage!);

		return this.httpClient.put(BASE_Url + id + '/bannerimage', formData).pipe(
			tap((response) => {
				this.user = response as User;
				this.user.profileImage = userDTO.profileImage;
				this.user.bannerImage = userDTO.bannerImage;
				this.user.imageURL = userDTO.profileImageUrl;
				this.user.bannerImageURL = userDTO.bannerImageUrl;
				localStorage.setItem('currentUser', JSON.stringify(this.user));
				console.log('Solicitud PUT de imagen de banner completada con éxito:', response);
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

	updateCurrentUser(user: User) {
		localStorage.setItem('currentUser', JSON.stringify(user));
	}
}
