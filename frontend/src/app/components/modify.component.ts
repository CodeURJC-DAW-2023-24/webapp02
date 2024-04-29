import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { SeriesService } from '../services/serie.service';
import { Component, Input } from '@angular/core';
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

  search(name: string, type: string): Observable<Element> {


    if (type == "LIBRO") {
      this.observableElement = this.bookService.getBookByName(name)
      var observableElementCopy = this.bookService.getBookByName(name).subscribe((response: any) => {
      this.elementSearched = response;
        // console.log(this.elementSearched.id)
      this.found = this.elementSearched != null
      return this.observableElement
  
  
        })
      // this.observableElement.subscribe((response: any) => {
      // this.elementSearched = response;
      // console.log(this.elementSearched.id)
      // this.found = this.elementSearched != null


      // })


    }

    else if (type == "PELICULA") {
      this.observableElement = this.filmService.getFilmByName(name)
      var observableElementCopy = this.filmService.getFilmByName(name).subscribe((response: any) => {
      this.elementSearched = response;
        // console.log(this.elementSearched.id)
      this.found = this.elementSearched != null
      return this.observableElement
      })
    }

    else if (type == "SERIE") {
      this.observableElement = this.seriesService.getSerieByName(name)
      var observableElementCopy = this.seriesService.getSerieByName(name).subscribe((response: any) => {
      this.elementSearched = response;
        // console.log(this.elementSearched.id)
      this.found = this.elementSearched != null
      return this.observableElement
      })
    }

    return this.observableElement

    
  }

  elementEdited(edited: boolean){
    this.found = !edited
    }
   

  
}
