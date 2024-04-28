import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { Element } from '../models/element.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SeriesService } from '../services/serie.service';
import { BooksService } from '../services/book.service';
import { FilmsService } from '../services/film.service';



@Component({
    selector: 'formComponent',
    templateUrl: '../Htmls/W-Form.component.html',
    styleUrl: '../Css/S-Header.css'
})
export class FormComponent implements OnChanges {


    @Input()
    observer!: Observable<Element>
    element!: Element;

    name: string = "";
    description: string = "";
    year: number = 0;
    type: string = "";
    season: string = "";
    state: string = "";
    author: string = ""
    country: string = "";
    generos: string[] = [];


    //genres : string[] = this.element.genres

    constructor(private http: HttpClient, private seriesService: SeriesService,
        private bookService: BooksService, private filmService: FilmsService, private router : Router
    ) { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['observer'] && this.observer) {


            this.observer.subscribe((response: any) => {
                this.element = response;
                this.name = this.element.name
                this.description = this.element.description
                this.author = this.element.author
                this.type = this.element.type
                if (this.type == "PELICULA") {
                    this.type = "PELÍCULA"
                }
                this.state = this.element.state
                this.season = this.element.season
                this.country = this.element.country
                this.year = this.element.year
                this.generos = this.element.generos

                // this.typeSelector(this.type, this.element);


            })

        }

    }



    public update() {
        if (this.type == "LIBRO") {
            this.changeElement();
            var elementEdited = this.bookService.addOrUpdateBook(this.element)
            elementEdited.subscribe()
            const imageInput = document.getElementById("imageInput") as HTMLInputElement;
            this.router.navigateByUrl("/Admin")
            if (imageInput && imageInput.files) {
                const imageFile = imageInput.files[0];
            }
        }

        else if (this.type == "PELICULA") {
            this.changeElement();
            this.filmService.addOrUpdateFilm(this.element)
            var elementEdited = this.filmService.addOrUpdateFilm(this.element)
            const imageInput = document.getElementById("imageInput") as HTMLInputElement;
            if (imageInput && imageInput.files) {
                const imageFile = imageInput.files[0];
            }
            this.router.navigateByUrl("/Admin")

        }


        else if (this.type == "SERIE") {
            this.changeElement();
            this.seriesService.addOrUpdateSerie(this.element)
            var elementEdited = this.seriesService.addOrUpdateSerie(this.element)
            const imageInput = document.getElementById("imageInput") as HTMLInputElement;
            if (imageInput && imageInput.files) {
                const imageFile = imageInput.files[0];
            }
            this.router.navigateByUrl("Admin")
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
        this.element.year = this.year;
        this.element.generos = this.generos;
    }
}