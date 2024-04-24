import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';
import { SeriesService } from '../services/serie.service';
import { Observable, concatMap, of } from 'rxjs';
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
  loadedElements: { [key: number]: Element[] } = {}; // elements per page


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
        this.getFilms(pageNum);
        break;
      case 'Series':
        this.getSeries(pageNum);
        break;
      default:
        break;
    }
  }

  getBooks(num: number){
    this.bookService.getBookPage(num).subscribe((response: any) => {
      this.totalPages = response.totalPages;
      this.hasPrev = response.pageable.pageNumber > 0;
      this.hasNext = response.pageable.pageNumber < response.totalPages - 1;
      this.loadedElements[this.page] = response.content;
      if(!this.elements$){
        this.elements$ = of([]);
      }
      const allLoadedBooks = Object.values(this.loadedElements).reduce((acc, val) => acc.concat(val), []);
      this.elements$ = of(allLoadedBooks);
    });
  }

  getFilms(num : number){
    this.filmsService.getFilmPage(num).subscribe((response: any) => {
      this.totalPages = response.totalPages;
      this.hasPrev = response.pageable.pageNumber > 0;
      this.hasNext = response.pageable.pageNumber < response.totalPages - 1;
      this.loadedElements[this.page] = response.content;
      if(!this.elements$){
        this.elements$ = of([]);
      }
      const allLoadedFilms = Object.values(this.loadedElements).reduce((acc, val) => acc.concat(val), []);
      this.elements$ = of(allLoadedFilms);
    });
  }

  getSeries(num: number){
    this.serieService.getSeriePage(num).subscribe((response: any) => {
      this.totalPages = response.totalPages;
      this.hasPrev = response.pageable.pageNumber > 0;
      this.hasNext = response.pageable.pageNumber < response.totalPages - 1;
      this.loadedElements[this.page] = response.content;
      if(!this.elements$){
        this.elements$ = of([]);
      }
      const allLoadedSeries = Object.values(this.loadedElements).reduce((acc, val) => acc.concat(val), []);
      this.elements$ = of(allLoadedSeries);
    });
  }

  loadPreviousPage(){
    delete this.loadedElements[this.page];
    this.page = this.page -1;
    this.getElements(this.libraryType, this.page);
  }

  loadNextPage(){
    this.page = this.page + 1;
    this.getElements(this.libraryType, this.page);
  }
}
