import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { Element } from '../models/element.model';

const BASE_URL_FILMS = '/api/films/';

@Injectable({ providedIn: 'root' })
export class FilmsService {

	constructor(private httpClient: HttpClient) { }

	getFilms(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL_FILMS).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element[]>;
	}

	get5Films(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL_FILMS + "top?page=0&size=5").pipe(
			map((response: any) => response.content),
			// Puedes agregar catchError aquí si lo necesitas
		);
	}

	//ask for 10 films
	getFilmPage(page: number): Observable<Element[]> {
		const url = `${BASE_URL_FILMS}?page=${page}&size=${10}`;
		return this.httpClient.get(url).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element[]>;
	}

	getFilm(id: number | string): Observable<Element> {
		return this.httpClient.get(BASE_URL_FILMS + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element>;
	}

	addOrUpdateFilm(Film: Element) {
		if (!Film.id) {
			return this.addFilm(Film);
		} else {
			return this.updateFilm(Film);
		}
	}

	private addFilm(Film: Element) {
		return this.httpClient.post(BASE_URL_FILMS, Film).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateFilm(Film: Element) {
		return this.httpClient.put(BASE_URL_FILMS + Film.id, Film).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeFilm(Film: Element) {
		return this.httpClient.delete(BASE_URL_FILMS + Film.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		if (error instanceof HttpErrorResponse) {
			// Manejar errores de HTTP
			console.error(`Error de HTTP: ${error.status}`);
			console.error(`Mensaje: ${error.message}`);
		} else {
			// Manejar otros tipos de errores
			console.error('Se produjo un error:');
			console.error(error);
		}
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}


	getFilmByName(name: string): Observable<Element> {
		return this.httpClient.get(BASE_URL_FILMS + name + "/").pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element>;
	}
}
