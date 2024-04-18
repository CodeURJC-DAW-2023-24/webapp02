import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Film } from './film.model';

const BASE_URL = '/api/Films/';

@Injectable({ providedIn: 'root' })
export class FilmsService {

	constructor(private httpClient: HttpClient) { }

	getFilms(): Observable<Film[]> {
		return this.httpClient.get(BASE_URL).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Film[]>;
	}

	getFilm(id: number | string): Observable<Film> {
		return this.httpClient.get(BASE_URL + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Film>;
	}

	addOrUpdateFilm(Film: Film) {
		if (!Film.id) {
			return this.addFilm(Film);
		} else {
			return this.updateFilm(Film);
		}
	}

	private addFilm(Film: Film) {
		return this.httpClient.post(BASE_URL, Film).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateFilm(Film: Film) {
		return this.httpClient.put(BASE_URL + Film.id, Film).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeFilm(Film: Film) {
		return this.httpClient.delete(BASE_URL + Film.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}
