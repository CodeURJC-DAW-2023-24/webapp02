import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';

import { Element } from '../models/element.model';

const BASE_URL_BOOKS = '/api/books/';

@Injectable({ providedIn: 'root' })
export class BooksService {

	constructor(private httpClient: HttpClient) { }

	getBooks(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL_BOOKS).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element[]>;
	}

	get5Books(): Observable<Element[]> {
		return this.httpClient.get(BASE_URL_BOOKS + "top?page=0&size=5").pipe(
			map((response: any) => response.content),
			// Puedes agregar catchError aquí si lo necesitas
		);
	}

	//ask for 10 books
	getBookPage(page: number): Observable<any> {
		const url = `${BASE_URL_BOOKS}?page=${page}&size=${10}`;
		return this.httpClient.get(url).pipe(
			tap((data: any) => {
				const totalPages = data.totalPages;
				const hasPrev = data.number > 0;
				const hasNext = data.number < totalPages - 1;
				const books = data.content;
				return { books, hasPrev, hasNext, totalPages } as any;
			})
			//catchError(error => this.handleError(error))
		)
	}

	getBook(id: number | string): Observable<Element> {
		return this.httpClient.get(BASE_URL_BOOKS + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element>;
	}


	getBookByName(name: string): Observable<Element> {
		var url = BASE_URL_BOOKS + name + "/"
		return this.httpClient.get(url).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<Element>;
	}

	addOrUpdateBook(book: Element) {
		if (!book.id) {
			return this.addBook(book);
		} else {
			return this.updateBook(book);
		}
	}

	private addBook(book: Element) {
		return this.httpClient.post(BASE_URL_BOOKS, book).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateBook(book: Element) {
		return this.httpClient.put(BASE_URL_BOOKS + book.id, book).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeBook(book: Element) {
		return this.httpClient.delete(BASE_URL_BOOKS + book.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}
}
