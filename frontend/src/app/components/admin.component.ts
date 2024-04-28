import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { SeriesService } from '../services/serie.service';
import { FilmsService } from '../services/film.service';
import { BooksService } from '../services/book.service';
import { ElementsService } from '../services/element.service';
import { Router } from '@angular/router';

const BASE_URL = "/api/series/";

@Component({
  selector: 'admin',
  templateUrl: '../Htmls/W-Admin.component.html',
  styleUrls: ['../Css/S-Main.css', "../Css/S-Admin.css", "../Css/S-NavBar.css"]
})
export class AdminComponent {



  constructor(private http: HttpClient, private seriesService: SeriesService,
    private bookService: BooksService, private filmService: FilmsService,
    private elementService : ElementsService, private router : Router) {     }

  // ngOnInit() {
  // }



  addElement(name: string, description: string, author: string, type: string, season: string, state: string, country: string, genres: string, years: string) {

    let yearsN = parseInt(years)

    const genresFormatted: string = genres.toUpperCase().trim();

    const genresArray: string[] = genresFormatted.split(',');

    const trimmedGenresArray: string[] = genresArray.map((genre: string) => genre.trim());

    const imageInput = document.getElementById("campo10") as HTMLInputElement;
    if (imageInput && imageInput.value && imageInput.files) {
      const imageFile = imageInput.files[0];
      this.http.post(BASE_URL,
        {
          name: name, description: description, author: author, year: yearsN, type: type,
          season: season, state: state, country: country, genres: trimmedGenresArray
        },
        { withCredentials: true }
      ).subscribe({

        next: () => {
          this.searchType(name, type, imageFile)
        },

        error: (err) => {
          console.log(err)
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

  searchType(name: string, type: string, imageFile: File) {
    if (type == "LIBRO") {

      var elementObserver = this.bookService.getBookByName(name)
      elementObserver.subscribe({
        next: (element) => {
          if (element.id) {
            this.bookService.uploadBookImage(element.id, imageFile).subscribe()
            window.location.reload()
          }
          window.location.reload()
        },
        error: (err) => {
          if (err.status != 404) {
            console.error('Error when asking if logged: ' + JSON.stringify(err));
          }
        }
      })


    }

    else if (type == "PELICULA" || type == "PELÍCULA") {
      type = "PELICULA"
      var elementObserver = this.filmService.getFilmByName(name)
      elementObserver.subscribe({
        next: (element) => {
          if (element.id) {
            this.filmService.addFilmImage(element.id, imageFile).subscribe()
            window.location.reload()
          }
          window.location.reload()
        },
        error: (err) => {
          if (err.status != 404) {
            console.error('Error when asking if logged: ' + JSON.stringify(err));
          }
        }
      })
    }


    else if (type == "SERIE") {
      var elementObserver = this.seriesService.getSerieByName(name)
      elementObserver.subscribe({
        next: (element) => {
          if (element.id) {
            this.seriesService.addSerieImage(element.id, imageFile).subscribe()
            window.location.reload()
          }            
          window.location.reload()
        },
        error: (err) => {
          if (err.status != 404) {
            console.error('Error when asking if logged: ' + JSON.stringify(err));
          }
        }
      })
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

  addGenre(genre: string) {
     this.elementService.addGenre(genre);
    // window.location.reload()
    
  }
}

