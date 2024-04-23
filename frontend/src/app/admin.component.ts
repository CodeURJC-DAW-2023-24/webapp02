import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Component, OnInit } from '@angular/core';

const BASE_URL = "/SingleElement/add";

@Component({
  selector: 'admin',
  templateUrl: './Htmls/W-Admin.component.html',
  styleUrls: ['./Css/S-Main.css', "./Css/S-Admin.css", "./Css/S-NavBar.css"]
})
export class AdminComponent {



  constructor(private http: HttpClient) { }

  // ngOnInit() {
  // }



  addElement(name: string, description: string, author: string, type: string, season: string, state: string, country: string, genres: string, years: number) {

    const imageInput = document.getElementById("campo10") as HTMLInputElement;
    if (imageInput && imageInput.files) {
      const imageFile = imageInput.files[0];
      this.http.post(BASE_URL,
        {
          name: name, description: description, author: author, type: type,
          season: season, state: state, country: country, genres: genres, years: years
        }).subscribe({
          next: (response) => {
            this.http.get("/Admin");
          },
          error: (err) => {
            alert("Wrong credentials");
          }
        });
    }

    else {
      this.http.post(BASE_URL, { name: name, description: description, author: author, type: type, season: season, state: state, country: country, genres: genres, years: years }).subscribe({
        next: (response) => {
          this.http.get("/Admin");
        },
        error: (err) => {
          alert("Wrong credentials");
        }
      });
    }
  }

  showForm(id: string) {
    const formElement = document.getElementById(id);
    if (formElement) {
      if (formElement.style.display === "none") {
        formElement.style.display = "block";
      } else {
        formElement.style.display = "none";
      }
    }
  }
}

