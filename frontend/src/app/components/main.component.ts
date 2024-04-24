import { Component, OnInit } from '@angular/core';
import { ElementsService } from '../services/element.service';
import { NewsService } from '../services/new.service';
import { Element } from '../models/element.model';
import { New } from '../models/new.model';
import { BooksService } from '../services/book.service';

@Component({
  selector: 'main',
  templateUrl: '../Htmls/W-Main.component.html',
  styleUrls: ['../Css/S-Main.css']
})
export class MainComponent implements OnInit {

  elements: Element[] = [];
  news: New[] = [];
  topBooks: Element[] = [];
  topFilms: Element[] = [];
  topSeries: Element[] = [];
  //cojo el top libros, top series y top pelis.

  constructor(private elementsService: ElementsService, private newsService: NewsService, private booksService: BooksService) { }

  ngOnInit(): void {
    this.getAllElements();
    this.get5RecentNews();
    this.get5Books();
  }

  getAllElements(): void {
    this.elementsService.getAllElements().subscribe({
      next: (response: Element[]) => {
        this.elements = response;
        // Otros procesamientos de la respuesta, si es necesario
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

  get5Books(): void{
    this.booksService.get5Books().subscribe({
      next: (books: Element[]) => {
        this.topBooks = books;
      },
      error: (error) => {
        console.error('Error al obtener el top libros:', error);
      }
    })
  }
  

}
