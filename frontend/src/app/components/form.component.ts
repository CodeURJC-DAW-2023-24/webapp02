import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { Element } from '../models/element.model';
import { Observable, windowWhen } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SeriesService } from '../services/serie.service';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';
import { Element as elementDTO } from '../models/elementDTO.model';

@Component({
    selector: 'formComponent',
    templateUrl: '../Htmls/W-Form.component.html',
    styleUrl: '../Css/S-Header.css'
})
export class FormComponent implements OnChanges {


    @Input()
    observer!: Observable<elementDTO> | Observable<Element>;

    @Output()
    edited = new EventEmitter<boolean>();

    element!: elementDTO;
    name: string = "";
    description: string = "";
    year: number = 0;
    type: string = "";
    season: string = "";
    state: string = "";
    author: string = ""
    country: string = "";
    genres: string[] = [];
    imageFile!: File

    //genres : string[] = this.element.genres

    constructor(private http: HttpClient, private seriesService: SeriesService,
        private bookService: BooksService, private filmService: FilmsService, private router: Router
    ) { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['observer'] && this.observer) {
            var observerElement = this.observer as Observable<Element>

            observerElement.subscribe((response: any) => {
                this.element = response
                this.name = response.name
                this.description = response.description
                this.author = response.author
                this.type = response.type
                // if (this.type == "PELICULA") {
                //     this.type = "PELÍCULA"
                // }
                this.state = response.state
                this.season = response.season
                this.country = response.country
                this.year = response.year
                this.genres = response.generos
                // this.typeSelector(this.type, this.element);
            })
        }
    }



    public update() {
        if (this.type == "LIBRO") {
            this.changeElement();
            this.bookService.addOrUpdateBook(this.element).subscribe({
                next: () => {
                    const imageInput = document.getElementById("imageInput") as HTMLInputElement;

                    if (imageInput && imageInput.value && imageInput.files) {
                        const imageFile = imageInput.files[0];
                        if (this.element.id) {
                            this.bookService.uploadBookImage(this.element.id, imageFile).subscribe()
                        }
                    }
                    this.edited.emit(true)
                }
            })
        }
        else if (this.type == "PELICULA" || this.type == "PELÍCULA") {
            this.changeElement();
            this.element.type = "PELICULA"
            this.filmService.addOrUpdateFilm(this.element).subscribe({
                next: () => {
                    const imageInput = document.getElementById("imageInput") as HTMLInputElement;
                    if (imageInput && imageInput.value && imageInput.files) {
                        const imageFile = imageInput.files[0];
                        if (this.element.id) {
                            this.filmService.uploadFilmImage(this.element.id, imageFile).subscribe()
                        }
                    }
                    this.edited.emit(true)
                }
            })
        }
        else if (this.type == "SERIE") {
            this.changeElement();
            this.seriesService.addOrUpdateSerie(this.element).subscribe({
                next: () => {
                    const imageInput = document.getElementById("imageInput") as HTMLInputElement;
                    if (imageInput && imageInput.value && imageInput.files) {
                        const imageFile = imageInput.files[0];
                        if (this.element.id) {
                            this.seriesService.uploadSerieImage(this.element.id, imageFile).subscribe()
                        }

                    }
                    this.edited.emit(true)
                }

            })

        }
    }

    private changeElement() {
        this.element.name = this.name;
        this.element.description = this.description;
        this.element.author = this.author;
        this.element.type = this.type
        this.element.state = this.state;
        this.element.season = this.season;
        this.element.country = this.country;
        // this.element.year = this.year;
        // const genresFormatted: string = this.genres.toUpperCase().trim();

        // const genresArray: string[] = genresFormatted.split(',');

        // const trimmedGenresArray: string[] = genresArray.map((genre: string) => genre.trim());
        this.element.genres = this.genres;



    }
}