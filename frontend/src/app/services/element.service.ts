import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Element } from '../models/element.model';

const BASE_URL = '/api/elements/';

@Injectable({ providedIn: 'root' })
export class ElementsService {

    constructor(private httpClient: HttpClient) { }

    getAllElements(): Observable<Element[]> {
        return this.httpClient.get<Element[]>(BASE_URL + "fiveNewElements")
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    console.error('Error:', error);
                    return throwError(() => new Error('Server error: ' + error.statusText));
                })
            );
    }

    getElementImage(id: number | string){
		return this.httpClient.get(BASE_URL + id + '/image' , { responseType: 'arraybuffer' })
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
}
