import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Element } from '../models/element.model';
import { Renderer2, ElementRef } from '@angular/core';
import Chart from 'chart.js/auto';
import { ProfileComponent } from '../components/profile.component';

@Component({
    selector: 'app-stats',
    templateUrl: '../Htmls/W-Stats.component.html',
    styleUrl: '../Css/S-Stats.css'
})
export class StatsComponent {

    @Input() elements: Element[] = [];
    previousElements: Element[] = [];
    typeNow: string | undefined;
    genresNow: string[] = [];
    genresUser: string[] = [];
    amountOfGenres: Map<string, number> = new Map;
    numBooks: number = 0;
    numFilms: number = 0;
    numSeries: number = 0;
    actualValue: number | undefined = 0;

    xValues2: string[] = [];
    yValues2: number[] = [];

    constructor(private profileComponent: ProfileComponent) { }

    //@Input() topImages: { [key: string]: string } = {};
    //@Input() title: String  | undefined;

    // async ngOnInit() {
    //     this.calculateStats();
    //     this.renderChart();
    //     this.renderChart2();
    // }
    // ngOnInit() {
    //     this.getAllElements().then(() => {
    //         this.calculateStats();
    //         this.renderChart();
    //         this.renderChart2();
    //     });
    // }

    // async getAllElements(): Promise<void> {
    //     if (!this.elements || this.elements.length === 0) {
    //         if (this.profileComponent && this.profileComponent.allElementsPromise) {
    //             this.elements = await this.profileComponent.allElementsPromise;
    //         }
    //     }
    // }
    ngOnInit() {
        this.profileComponent.dataLoaded.subscribe(() => {
            this.calculateStats();
            //Suscribirme a elements y esperar a que no sea nulo para ejecutar calculatestats
            this.renderChart();
            this.renderChart2();
        });
    }

    // angOnChanges(changes: SimpleChanges):void {
    //     if (changes['elements'] && this.elements) {
    //         this.calculateStats();
    //         this.renderChart();
    //         this.renderChart2();
    //         // this.typeSelector(this.type, this.element);
    //     }
    // }

    // ngOnCheck() {
    //     if (this.elements !== this.previousElements) {
    //         console.log(`Value changed from ${this.previousElements} to ${this.elements}`);
    //         this.previousElements = this.elements;
    //     }
    // }


    calculateStats() {
        for (let elementX of this.elements) {
            this.typeNow = elementX.type;
            switch (this.typeNow) {
                case "LIBRO":
                    this.numBooks = this.numBooks + 1;
                    break;
                case "PELICULA":
                    this.numFilms = this.numFilms + 1;
                    break;
                case "SERIE":
                    this.numSeries = this.numSeries + 1;
                    break;
            }
            this.genresNow = elementX.generos;
            for (let currentGenre of this.genresNow) {
                if (!this.amountOfGenres.has(currentGenre)) {
                    this.amountOfGenres.set(currentGenre, 1);
                } else {
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
        const canvas = document.getElementById('myChart') as HTMLCanvasElement;
        const ctx = canvas.getContext('2d');
        if (ctx) {
            ctx.font = '24px Arial';
            ctx.textAlign = 'center';
            ctx.fillText('Medios Guardados', canvas.width / 2, 30);
        }
    } //end of renderChart1

    renderChart2() {
        for (let [key, value] of this.amountOfGenres) {
            //console.log(key, value);
            this.xValues2.push(key);
            this.yValues2.push(value);
        }
        //const xValues = ["LIBROS", "PELICULAS", "SERIES"];
        //const yValues = [this.numBooks, this.numFilms, this.numSeries];
        const barColors = ["red", "black", "#5cd65c", "orange", "yellow", "#00ffcc", "purple", "#99ccff", "#0000b3", "#ffb3cc", "#ffbf80", "#cc0066", "brown"];
        new Chart('myChart2', {
            type: 'pie',
            data: {
                labels: this.xValues2,
                datasets: [{
                    backgroundColor: barColors,
                    data: this.yValues2
                }]
            }
        });
        const canvas = document.getElementById('myChart2') as HTMLCanvasElement;
        const ctx = canvas.getContext('2d');
        if (ctx) {
            ctx.font = '24px Arial';
            ctx.textAlign = 'center';
            ctx.fillText('Géneros más gustados', canvas.width / 2, 30);
        }
    } //end of renderChart1
}