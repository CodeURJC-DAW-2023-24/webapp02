import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'modifyFragment',
  templateUrl: '../Htmls/W-ModifyFragment.component.html',
  styleUrls: ['../Css/S-Main.css']
})
export class ModifyFragment  {
  csrfToken: string | undefined;

  constructor(private http: HttpClient) { }

  // ngOnInit() {
  //   this.fetchCsrfToken();
  // }

  // fetchCsrfToken() {
  //   of({ token: 'your_token_here' }) // Replace 'your_token_here' with the actual token value.
  //     .pipe(
  //       catchError((error: HttpErrorResponse) => {
  //         console.error('Error al obtener el token CSRF:', error);
  //         return of(null);
  //       })
  //     )
  //     .subscribe({
  //       next: (response) => {
  //         if (response && response.token) {
  //           this.csrfToken = response.token;
  //         }
  //       },
  //       error: (err) => console.log('error:', err),
  //       complete: () => console.log('the end'),
  //     });
  // }
}