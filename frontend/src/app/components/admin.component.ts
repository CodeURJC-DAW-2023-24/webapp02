import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Component, Inject, OnInit } from '@angular/core';
import { SeriesService } from '../services/serie.service';
import { FilmsService } from '../services/film.service';
import { BooksService } from '../services/book.service';
import { ElementsService } from '../services/element.service';
import { Element as elementDTO } from '../models/elementDTO.model';
import { Router } from '@angular/router';
import { New } from '../models/new.model';
import { NewsService } from '../services/new.service';


const BASE_URL = "/api/series/";

@Component({
  selector: 'admin',
  templateUrl: '../Htmls/W-Admin.component.html',
  styleUrls: ["../Css/S-Main.css", "../Css/S-Admin.css", "../Css/S-NavBar.css"]
})
export class AdminComponent {



  public hiddenAdd:boolean = true
  public hiddenGenre:boolean = true

  actualNew: New | undefined;

  constructor(private http: HttpClient, private seriesService: SeriesService,
    private bookService: BooksService, private filmService: FilmsService,
    private elementService: ElementsService, private newsService: NewsService) { }

  // ngOnInit() {
  // }

  addNew(title: string, date: string, description: string, link: string){
    const localeDate: Date = new Date(date);
    const new1: New = {
      title: title,
      description: description,
      date: localeDate,
      link: link
    };

    this.newsService.addNews(new1).subscribe({
      next: (response: any) => {
        this.actualNew = response as New;
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }

  addElement(name: string, description: string, author: string, type: string, season: string, state: string, country: string, genres: string, years: string) {

    let yearsN = parseInt(years)

    const genresFormatted: string = genres.toUpperCase().trim();

    const genresArray: string[] = genresFormatted.split(',');

    const trimmedGenresArray: string[] = genresArray.map((genre: string) => genre.trim());

    const imageInput = document.getElementById("campo10") as HTMLInputElement;
    if (imageInput && imageInput.value && imageInput.files) {
      const imageFile = imageInput.files[0];
      if (type == "SERIE") {
        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN);
        this.seriesService.addOrUpdateSerie(element).subscribe({
          next: (elementSubscribed) => {
            this.searchType(name,type,imageFile)
            this.hiddenAdd = true
          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })

      }
      else if (type == "LIBRO") {

        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN);
        this.bookService.addOrUpdateBook(element).subscribe({
          next: (elementSubscribed) => {
            this.searchType(name,type,imageFile)
            this.hiddenAdd = true

          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })


      }
      else if (type == "PELICULA" || type == "PELÍCULA") {
        type = "PELICULA"
        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN);
        this.filmService.addOrUpdateFilm(element).subscribe({
          next: (elementSubscribed) => {
            this.searchType(name,type,imageFile)
            this.hiddenAdd = true
          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })

      }



    }

    else {
      if (type == "SERIE") {
        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN);
        this.seriesService.addOrUpdateSerie(element).subscribe({
          next: () => {
            this.hiddenAdd = true

          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })

      }

      else if (type == "LIBRO") {

        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN)
        this.bookService.addOrUpdateBook(element).subscribe({
          next: () => {
            this.hiddenAdd = true

          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })


      }

      else if (type == "PELICULA" || type == "PELÍCULA") {
        type = "PELICULA"
        var element = this.makeElement(name, description, author, type, season, state, country, trimmedGenresArray, yearsN)
        this.filmService.addOrUpdateFilm(element).subscribe({
          next: () => {
            this.hiddenAdd = true

          },
          error: (err) => {
            if (err.status != 404) {
              console.error('Error:' + JSON.stringify(err));
            }
          }
        })

      }

    }
  }

  searchType(name: string, type: string, imageFile: File) {
    if (type == "LIBRO") {

      var elementObserver = this.bookService.getBookByName(name)
      elementObserver.subscribe({
        next: (element) => {
          if (element.id) {
            this.bookService.addBookImage(element.id, imageFile).subscribe()
          }
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
          }
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
          }
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




  showFormAdmin() {
    this.hiddenAdd = !this.hiddenAdd

  }

  showFormAdminGenre() {
    this.hiddenGenre = !this.hiddenGenre

  }

  addGenre(genre: string) {
    this.elementService.addGenre(genre);
    this.hiddenGenre = true;
    // window.location.reload()

  }

  makeElement(name: string, description: string, author: string, type: string, season: string, state: string, country: string, generos: string[], years: number): elementDTO {
    // Crear una nueva instancia de Element

    const newElement: elementDTO = {
      name: name,
      description: description,
      year: years,
      type: type,
      season: season,
      state: state,
      author: author,
      country: country,
      genres: generos,
      imageFile: new Blob(),
      reviews: []
    };


    // Asignar los valores recibidos como parámetros a las propiedades del nuevo elemento
    newElement.name = name;
    newElement.description = description;
    newElement.author = author;
    newElement.type = type;
    newElement.season = season;
    newElement.state = state;
    newElement.country = country;
    newElement.genres = generos;
    newElement.year = years;



    // Ahora puedes usar el nuevo elemento como desees
    return newElement

    // Resto de tu lógica aquí...
  }


}

