import { Component, Input } from '@angular/core';
import { Element } from '../models/element.model';

@Component({
    selector: 'app-stats',
    templateUrl: '../Htmls/W-Stats.component.html',
    styleUrl: '../Css/S-Stats.css'
})
export class StatsComponent {
    //@Input() numBooks: number = 0; 
    @Input() elements: Element[] = [];
    typeNow: string | undefined;
    genresNow: string[] = [];
    genresUser: string[]=[];
    numBooks: number =0;
    numFilms: number =10;
    numSeries: number =0;

    //@Input() topImages: { [key: string]: string } = {};
    //@Input() title: String  | undefined;

    ngOnInit(){
        this.calculateStats();
    }

    calculateStats(){
        for(let elementX of this.elements){
            this.typeNow = elementX.type;
            switch(this.typeNow){
                case "LIBRO":
                    this.numBooks = this.numBooks +1;
                    break;
                case "PELICULA":
                    this.numFilms = this.numFilms + 1;
                    break;
                case "SERIE":
                    this.numSeries = this.numSeries + 1;
                    break;
            }
            this.genresNow = elementX.generos;
            for(let currentGenre of this.genresNow){

            }
        }
    }
  }