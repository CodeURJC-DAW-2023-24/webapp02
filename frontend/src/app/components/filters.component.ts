import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BooksService } from '../services/book.service';
import { SeriesService } from '../services/serie.service';
import { FilmsService } from '../services/film.service';
import { Observable } from 'rxjs';
import { Element as ElementComponent } from '../models/element.model';


@Component({
  selector: 'lib-filter',
  templateUrl: '../Htmls/W-Filters.component.html',
  styleUrl: '../Css/S-Library.css'
})
export class FilterComponent {
  selectedGenre:string="";
  selectedSeason:string="";
  selectedCountry:string="";
  selectedState:string="";

  @Input()
  elementType:string| null = null;
  @Output()
  filteredElementsOutput: EventEmitter<ElementComponent[]> = new EventEmitter();

  filteredElements!: ElementComponent[];

  constructor(private bookService: BooksService, private serieService: SeriesService, private filmService: FilmsService){}

  //we filter and send the filtered elements to the father's component
  genreFilter($event: any){
    $event.preventDefault();
    switch(this.elementType){
      case 'Books':
        this.bookService.getBookByFilter("genre", this.selectedGenre).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Films':
        this.filmService.getFilmByFilter("genre", this.selectedGenre).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Series':
        this.serieService.getSerieByFilter("genre", this.selectedGenre).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      default:
        break;
    }

  }

  seasonFilter($event: any){
    $event.preventDefault();
    switch(this.elementType){
      case 'Books':
        this.bookService.getBookByFilter("season", this.selectedSeason).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Films':
        this.filmService.getFilmByFilter("season", this.selectedSeason).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Series':
        this.serieService.getSerieByFilter("season", this.selectedSeason).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      default:
        break;
    }

  }

  countryFilter($event: any){
    $event.preventDefault();
    switch(this.elementType){
      case 'Books':
        this.bookService.getBookByFilter("country", this.selectedCountry).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Films':
        this.filmService.getFilmByFilter("country", this.selectedCountry).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Series':
        this.serieService.getSerieByFilter("country", this.selectedCountry).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      default:
        break;
    }

  }

  stateFilter($event: any){
    $event.preventDefault();
    switch(this.elementType){
      case 'Books':
        this.bookService.getBookByFilter("state", this.selectedState).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Films':
        this.filmService.getFilmByFilter("state", this.selectedState).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      case 'Series':
        this.serieService.getSerieByFilter("state", this.selectedState).subscribe((response: any) => {
          this.filteredElements = response.content;
          this.filteredElementsOutput.emit(this.filteredElements);
        });
        break;
      default:
        break;
    }

  }
}
