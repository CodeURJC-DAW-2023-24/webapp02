import { Component, OnInit } from '@angular/core';
import { LoginService } from '../services/login.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';
import { SeriesService } from '../services/serie.service';
import { Observable, of } from 'rxjs';
import { Element } from '../models/element.model';


@Component({
  selector: 'library',
  templateUrl: '../Htmls/W-Library.component.html',
  styleUrls: ['../Css/S-Main.css', '../Css/S-Library.css']
})
export class LibraryComponent implements OnInit{

  libraryType: string | null = null;
  elements$: Observable<Element[]> | undefined;
  page: number = 0;
  totalPages: number = 0;
  hasPrev: boolean = false;
  hasNext: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private bookService: BooksService,
    private filmsService: FilmsService, private serieService: SeriesService){}

  ngOnInit(): void {
    this.libraryType = this.route.snapshot.paramMap.get('type');
    this.getElements(this.libraryType, 0);
  }

  getElements(type: string | null, pageNum: number): void{
    switch(type){
      case 'Books':
        this.getBooks(pageNum);
        break;
      case 'Films':
        this.getFilms();
        break;
      case 'Series':
        this.getSeries();
        break;
      default:
        //observable vacío
    }
  }

  getBooks(num: number){
    this.bookService.getBookPage(num).subscribe((response: any) => {
      this.totalPages = response.totalPages;
      this.hasPrev = response.pageable.pageNumber > 0;
      this.hasNext = response.pageable.pageNumber < response.totalPages - 1;
      this.elements$ = response.content;
    });
  }

  getFilms(){
    return this.filmsService.getFilmPage(this.page);
  }

  getSeries(){
    return this.serieService.getSeriePage(this.page);
  }

  loadPreviousPage(){
    this.getElements(this.libraryType, this.page-1);
  }

  loadNextPage(){
    this.getElements(this.libraryType, this.page+1);
  }
}
