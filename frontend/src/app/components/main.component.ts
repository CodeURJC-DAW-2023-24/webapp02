import { Component, OnInit } from '@angular/core';
import { ElementsService } from '../services/element.service';
import { NewsService } from '../services/new.service';
import { Element } from '../models/element.model';
import { New } from '../models/new.model';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';
import { SeriesService } from '../services/serie.service';

@Component({
  selector: 'main',
  templateUrl: '../Htmls/W-Main.component.html',
  styleUrls: ['../Css/S-Main.css']
})
export class MainComponent implements OnInit {

  elements: Element[] = [];
  elementsImages: { [key: string]: string } = {};

  news: New[] = [];

  topBooks: Element[] = [];
  topBooksImages: { [key: string]: string } = {};

  topFilms: Element[] = [];
  topFilmsImages: { [key: string]: string } = {};

  topSeries: Element[] = [];
  topSeriesImages: { [key: string]: string } = {};

  constructor(private elementsService: ElementsService, private newsService: NewsService, private booksService: BooksService, private filmsService: FilmsService, private seriesService: SeriesService) { }

  ngOnInit(): void {
    this.getAllElements();
    this.get5RecentNews();
    this.get5Books();
    this.get5Films();
    this.get5Series();
  }

  getAllElements(): void {
    this.elementsService.getAllElements().subscribe({
      next: (response: Element[]) => {
        this.elements = response;
        for (let element of response) {
          if (element.id !== undefined) {
            this.elementsService.getElementImage(element.id).subscribe((imageData) => {
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.elementsImages[element.name] = URL.createObjectURL(blob)
              } else {
                this.elementsImages[element.name] = ''
              }
            });
          }
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  get5RecentNews(): void {
    this.newsService.get5RecentNews().subscribe({
      next: (news: New[]) => {
        this.news = news;
      },
      error: (error) => {
        console.error('Error al obtener las noticias:', error);
      }
    });
  }

  get5Books(): void {
    this.booksService.get5Books().subscribe({
      next: (books: Element[]) => {
        this.topBooks = books;
        for (let book of books) {
          if (book.id !== undefined) {
            this.booksService.getBookImage(book.id).subscribe((imageData) => {
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.topSeriesImages[book.name] = URL.createObjectURL(blob)
              } else {
                this.topSeriesImages[book.name] = ''
              }
            });
          }
        }
      },
      error: (error) => {
        console.error('Error al obtener el top libros:', error);
      }
    })
  }

  get5Films(): void {
    this.filmsService.get5Films().subscribe({
      next: (films: Element[]) => {
        this.topFilms = films;
        for (let film of films) {
          if (film.id !== undefined) {
            this.filmsService.getFilmImage(film.id).subscribe((imageData) => {
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.topSeriesImages[film.name] = URL.createObjectURL(blob)
              } else {
                this.topSeriesImages[film.name] = ''
              }
            });
          }
        }
      },
      error: (error) => {
        console.error('Error al obtener el top libros:', error);
      }
    })
  }

  get5Series(): void {
    this.seriesService.get5Series().subscribe({
      next: (series: Element[]) => {
        this.topSeries = series;
        for (let serie of series) {
          if (serie.id !== undefined) {
            this.seriesService.getSerieImage(serie.id).subscribe((imageData) => {
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.topSeriesImages[serie.name] = URL.createObjectURL(blob)
              } else {
                this.topSeriesImages[serie.name] = ''
              }
            });
          }
        }
      },
      error: (error) => {
        console.error('Error al obtener el top libros:', error);
      }
    })
  }


}
