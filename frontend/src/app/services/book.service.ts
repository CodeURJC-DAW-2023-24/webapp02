import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';

import { Element as ElementComponent } from '../models/element.model';

const BASE_URL_BOOKS = '/api/books/';

@Injectable({ providedIn: 'root' })
export class BooksService {
  

	constructor(private httpClient: HttpClient) { }

	getBooks(): Observable<ElementComponent[]> {
		return this.httpClient.get(BASE_URL_BOOKS).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<ElementComponent[]>;
	}

	get5Books(): Observable<ElementComponent[]> {
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

	getBook(id: number | string): Observable<ElementComponent> {
		return this.httpClient.get(BASE_URL_BOOKS + id).pipe(
			//catchError(error => this.handleError(error))
		) as Observable<ElementComponent>;
	}

	getBookImage(id: number | string){
		return this.httpClient.get(BASE_URL_BOOKS + id + '/image' , { responseType: 'arraybuffer' })
	}


	uploadBookImage(id: number | string, file: File){
		let imageFile = new FormData();
		imageFile.append("imageFile",file)
		
		return this.httpClient.put(BASE_URL_BOOKS + id + '/image' ,imageFile )
	}

	addBookImage(id: number | string, file: File){
		let imageFile = new FormData();
		imageFile.append("imageFile",file)
		
		return this.httpClient.post(BASE_URL_BOOKS + id + '/image' ,imageFile )
	}
  //we make a get petition to get the books with the filter we want applied
  getBookByFilter(filterType:string, filter:string): Observable<ElementComponent[]>{
    switch(filterType){
      case 'genre':
        return this.httpClient.get(BASE_URL_BOOKS+ filterType + '?filter=' + filter).pipe(
          //catchError(error => this.handleError(error))
        ) as Observable<ElementComponent[]>;
      case 'season':
        return this.httpClient.get(BASE_URL_BOOKS+ filterType + '?filter=' + filter).pipe(
          //catchError(error => this.handleError(error))
        ) as Observable<ElementComponent[]>;
      case 'country':
        return this.httpClient.get(BASE_URL_BOOKS+ filterType + '?filter=' + filter).pipe(
          //catchError(error => this.handleError(error))
        ) as Observable<ElementComponent[]>;
      case 'state':
        return this.httpClient.get(BASE_URL_BOOKS+ filterType + '?filter=' + filter).pipe(
          //catchError(error => this.handleError(error))
        ) as Observable<ElementComponent[]>;
      default:
        return of([]);
    }
  }

	addOrUpdateBook(book: ElementComponent) {
		if (!book.id) {
			return this.addBook(book);
		} else {
			return this.updateBook(book);
		}
	}

	private addBook(book: ElementComponent) {
		return this.httpClient.post(BASE_URL_BOOKS, book).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateBook(book: ElementComponent) {
		return this.httpClient.put(BASE_URL_BOOKS + book.id, book).pipe(
			catchError(error => this.handleError(error))
		);
	}

	removeBook(book: ElementComponent) {
		return this.httpClient.delete(BASE_URL_BOOKS + book.id).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}


	getBookByName(name: string): Observable<ElementComponent> {
		return this.httpClient.get(BASE_URL_BOOKS + name + "/").pipe(
			//catchError(error => this.handleError(error))
		) as Observable<ElementComponent>;
	  }
}
