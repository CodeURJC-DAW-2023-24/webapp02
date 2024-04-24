import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { New } from '../models/new.model';

const BASE_URL = '/api/news/';

@Injectable({ providedIn: 'root' })
export class NewsService {

    constructor(private httpClient: HttpClient) { }

    get5RecentNews(): Observable<New[]> {
        return this.httpClient.get<New[]>(BASE_URL + 'fiveRecentNews')
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    console.error('Error:', error);
                    return throwError(() => new Error('Server error: ' + error.statusText));
                })
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
}
