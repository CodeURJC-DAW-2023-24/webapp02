import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Element } from './element.model';

const BASE_URL = '/api/series/';

@Injectable({ providedIn: 'root' })
export class SeriesService {

	constructor(private httpClient: HttpClient) { }

	getSeries(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element[]>;
	}

	getSerie(id: number | string): Observable<Element> {
		return this.httpClient.get(BASE_URL + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element>;
	}

	addOrUpdateSerie(Serie: Element) {
		if (!Serie.id) {
			return this.addSerie(Serie);
		} else {
			return this.updateSerie(Serie);
		}
	}

	private addSerie(Serie: Element) {
		return this.httpClient.post(BASE_URL, Serie).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateSerie(Serie: Element) {
		return this.httpClient.put(BASE_URL + Serie.id, Serie).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeSerie(Serie: Element) {
		return this.httpClient.delete(BASE_URL + Serie.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}
}
