import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';
import { SeriesService } from '../services/serie.service';
import { Observable, concatMap, of } from 'rxjs';
import { Element as ElementComponent } from '../models/element.model';
import { ElementsService } from '../services/element.service';


@Component({
  selector: 'library',
  templateUrl: '../Htmls/W-Library.component.html',
  styleUrls: ['../Css/S-Main.css', '../Css/S-Library.css']
})
export class LibraryComponent implements OnInit{

  libraryType: string | null = null;
  title: string | null = null;
  elements$: Observable<ElementComponent[]> | undefined;
  page: number = 0;
  totalPages: number = 0;
  hasPrev: boolean = false;
  hasNext: boolean = false;
  loadedElements: { [key: number]: ElementComponent[] } = {}; // elements per page
  elementImages: { [key: string]: string } = {};


  constructor(private route: ActivatedRoute, private router: Router, private bookService: BooksService,
    private filmsService: FilmsService, private serieService: SeriesService, private elementService: ElementsService){}

  ngOnInit(): void {
    //me suscribo a cualquier cambio del parametro ed la ruta
    this.route.paramMap.subscribe(params => {
      this.libraryType = params.get('type');
      if (this.libraryType == 'Books'){
        this.title = 'Libros';
      }else if (this.libraryType == 'Films'){
        this.title = 'Peliculas';
      }
      else if (this.libraryType == 'Series'){
        this.title = 'Series';
      }
      this.getElements(this.libraryType, 0);
      this.loadedElements = {};
    });
  }

  filteredElements(filteredElements : ElementComponent[]){
    this.loadedElements = {};
    this.loadedElements[0] = filteredElements;
    this.elements$ = of(filteredElements);
    this.hasNext = false;
    this.hasPrev = false;
    this.loadImages();
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
      this.loadImages();
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
      this.loadImages();
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
      this.loadImages();
      if(!this.elements$){
        this.elements$ = of([]);
      }
      const allLoadedSeries = Object.values(this.loadedElements).reduce((acc, val) => acc.concat(val), []);
      this.elements$ = of(allLoadedSeries);
    });
  }

  loadPreviousPage(){
    //delete this.loadedElements[this.page];
    this.page = this.page -1;
    this.hasPrev = this.page > 0;
    this.hasNext = this.page < Object.keys(this.loadedElements).length - 1;

    const allLoadedElements = [];
    for (let key in this.loadedElements) {
      if (parseInt(key, 10) <= this.page) {
          // Adds all elements to the constant allLoadedElements
          allLoadedElements.push(...this.loadedElements[key]);
      }
    }
    this.elements$ = of(allLoadedElements);
  }

  loadNextPage(){
    this.page = this.page + 1;
    if(this.loadedElements[this.page]!= null){
      const allLoadedElements = [];
      for (let key in this.loadedElements) {
        if (parseInt(key, 10) <= this.page) {
            // Adds all elements to the constant allLoadedElements
            allLoadedElements.push(...this.loadedElements[key]);
        }
      }
      this.elements$ = of(allLoadedElements);
      this.hasPrev = this.page > 0;
      this.hasNext = this.page < Object.keys(this.loadedElements).length - 1;
    }
    else{
      this.getElements(this.libraryType, this.page);
    }
  }

  loadImages(){
    for (const key in this.loadedElements) {
      if (this.loadedElements.hasOwnProperty(key)) {
        const elementsArray = this.loadedElements[key];
        for (const element of elementsArray) {
          if (element.id !== undefined) {
            this.elementService.getElementImage(element.id).subscribe((imageData) => {
              if (imageData) {
                const blob = new Blob([imageData], { type: 'image/jpeg' });
                this.elementImages[element.name] = URL.createObjectURL(blob)
              } else {
                this.elementImages[element.name] = ''
              }
            });
          }
        }
      }
    }
  }
}
