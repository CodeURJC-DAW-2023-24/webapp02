import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { SeriesService } from '../services/serie.service';
import { Component } from '@angular/core';
import { Element } from '../models/element.model';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';


@Component({
  selector: 'modifyFragment',
  templateUrl: '../Htmls/W-ModifyFragment.component.html',
  styleUrls: ['../Css/S-Main.css']
})

export class ModifyComponent {

  public found: boolean | undefined;
  public elementSearched!: Element;
  public observableElement! : Observable<Element>
  public observableElementCopy! : Observable<Element>  


  constructor(private http: HttpClient, private seriesService: SeriesService,
    private bookService: BooksService, private filmService: FilmsService
  ) { }

  // edit(name: string , type:string) {
  //   const BASE_URL = "/api/series/";

  //   const elementsArray: Element[] = [];
  //   const observerElement = this.search(name);
  //   this.searchElement(observerElement, elementsArray, name);

  //   const resultsContainer = document.getElementById('modifyBookForm');
  // }

  search(name: string, type: string): Observable<Element[]> {


    if (type == "LIBRO") {
      this.observableElement = this.bookService.getBookByName(name)
      var observableElementCopy = this.bookService.getBookByName(name).subscribe((response: any) => {
      this.elementSearched = response;
        // console.log(this.elementSearched.id)
      this.found = this.elementSearched != null
  
  
        })
      // this.observableElement.subscribe((response: any) => {
      // this.elementSearched = response;
      // console.log(this.elementSearched.id)
      // this.found = this.elementSearched != null


      // })


    }

    else if (type == "PELICULA") {
      this.filmService.getFilmByName(name).subscribe((response: any) => {
      this.elementSearched = response;
      this.found = this.elementSearched != null
      })
    }

    else if (type == "SERIE") {
      this.bookService.getBookByName(name).subscribe((response: any) => {
      this.elementSearched = response;
      this.found = this.elementSearched != null
      })
    }


    const books$ = this.getArray(this.bookService.getBooks());
    const series$ = this.getArray(this.seriesService.getSeries());
    const films$ = this.getArray(this.filmService.getFilms());

    return forkJoin([books$, series$, films$]).pipe(
      map(([booksArray, seriesArray, filmsArray]) => {
        const elementsArray: Element[] = [];
        elementsArray.push(...booksArray, ...seriesArray, ...filmsArray);
        return elementsArray.filter(element => element.name === name);
      })
    );
  }

  private searchElement(booksArray: Observable<Element[]>, elementsArray: Element[], name: string): Element[] {
    booksArray.subscribe({
      next: (elements) => {
        elementsArray.concat(elements);
        var size = elements.length;
        if (size === 1) {
          var element = elements[0]



          // this.http.get('assets/W-EditFragment.html', { responseType: 'text' })
          //   .subscribe({
          //     next: (html: string) => {
          //       const resultsContainer = document.getElementById('modifyBookForm');
          //       if (resultsContainer) {
          //         resultsContainer.innerHTML = html;
          //       }
          //     },
          //     error: (err: any) => {
          //       console.error('Error al cargar el contenido HTML:', err);
          //     }
          //   });
        }
        else if (size == 2) {
          var types: string[] = []
          for (let i = 0; i < 2; i++) {
            types.push(elements[i].type)
          }

        }
      }
    });
    return elementsArray;
  }

  private searchType(element: Element) {
    if (element.type === "LIBRO") {
      if (element.id) {
        var elementFound = this.bookService.getBook(element.id);
        elementFound.subscribe();
      }
    } else if (element.type === "SERIE") {
      if (element.id) {
        var elementFound = this.seriesService.getSerie(element.id);
        elementFound.subscribe();
      }
    } else if (element.type === "PELICULA") {
      if (element.id) {
        var elementFound = this.filmService.getFilm(element.id);
        elementFound.subscribe();
      }
    }
  }

  getArray(jsonElement: Observable<Element[]>): Observable<Element[]> {
    return jsonElement.pipe(
      map((jsonElement) => {
        const element = Object.entries(jsonElement);
        return element[0][1] as unknown as Element[];
      })
    );
  }
}
