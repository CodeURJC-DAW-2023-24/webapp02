import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Serie } from './serie.model';

const BASE_URL = '/api/series/';

@Injectable({ providedIn: 'root' })
export class SeriesService {

	constructor(private httpClient: HttpClient) { }

	getSeries(): Observable<Serie[]> {
		return this.httpClient.get(BASE_URL).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Serie[]>;
	}

	getSerie(id: number | string): Observable<Serie> {
		return this.httpClient.get(BASE_URL + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Serie>;
	}

	addOrUpdateSerie(Serie: Serie) {
		if (!Serie.id) {
			return this.addSerie(Serie);
		} else {
			return this.updateSerie(Serie);
		}
	}

	private addSerie(Serie: Serie) {
		return this.httpClient.post(BASE_URL, Serie).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateSerie(Serie: Serie) {
		return this.httpClient.put(BASE_URL + Serie.id, Serie).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeSerie(Serie: Serie) {
		return this.httpClient.delete(BASE_URL + Serie.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}
