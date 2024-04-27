import { Component, Input, OnInit } from '@angular/core';
import { Element } from '../models/element.model';
import { Renderer2, ElementRef } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
    selector: 'app-stats',
    templateUrl: '../Htmls/W-Stats.component.html',
    styleUrl: '../Css/S-Stats.css'
})
export class StatsComponent {

    @Input() elements: Element[] = [];
    typeNow: string | undefined;
    genresNow: string[] = [];
    genresUser: string[]=[];
    amountOfGenres: Map<string, number> = new Map;
    numBooks: number =0;
    numFilms: number =0;
    numSeries: number =0;
    actualValue: number | undefined=0;

    //@Input() topImages: { [key: string]: string } = {};
    //@Input() title: String  | undefined;

    ngOnInit(){
        this.calculateStats();
        this.renderChart();

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
                if(!this.amountOfGenres.has(currentGenre)){
                    this.amountOfGenres.set(currentGenre, 1);
                } else{
                    this.actualValue = this.amountOfGenres.get(currentGenre);
                    this.actualValue = this.actualValue! + 1;
                    this.amountOfGenres.set(currentGenre, this.actualValue);
                    this.actualValue = 0;
                }
            }
        }
    }
    renderChart() {
        const xValues = ["LIBROS", "PELICULAS", "SERIES"];
        const yValues = [this.numBooks, this.numFilms, this.numSeries];
        const barColors = ["red", "blue", "#5cd65c"];
        new Chart('myChart', {
            type: 'pie',
            data: {
                labels: xValues,
                datasets: [{
                    backgroundColor: barColors,
                    data: yValues
                }]
            }
        });
        // Establecer el título del gráfico directamente en el canvas
        const canvas = document.getElementById('myChart') as HTMLCanvasElement;
        const ctx = canvas.getContext('2d');
        if (ctx) {
            ctx.font = '24px Arial';
            ctx.textAlign = 'center';
            ctx.fillText('Medios Guardados', canvas.width / 2, 30);
        }
    }
  }