import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { Element } from '../models/element.model';

const BASE_URL = '/api/series/';

@Injectable({ providedIn: 'root' })
export class SeriesService {

	constructor(private httpClient: HttpClient) { }

	getSeries(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element[]>;
	}

	get5Series(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL + "top?page=0&size=5").pipe(
			map((response: any) => response.content),
			// Puedes agregar catchError aquí si lo necesitas
		);
	}

	getSerieImage(id: number | string){
		return this.httpClient.get(BASE_URL + id + '/image' , { responseType: 'arraybuffer' })
	}
	//ask for 10 series
	getSeriePage(page: number): Observable<Element[]> {
		const url = `${BASE_URL}?page=${page}&size=${10}`;
		return this.httpClient.get(url).pipe(
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
