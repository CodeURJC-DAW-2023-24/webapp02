import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { SeriesService } from '../services/serie.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'modifyFragment',
  templateUrl: '../Htmls/W-ModifyFragment.component.html',
  styleUrls: ['../Css/S-Main.css']
})



export class ModifyFragment {

  editFragment: string | undefined;
  selectedSeason = 'OTOÑO'
  constructor(private http: HttpClient, private seriesService : SeriesService) { }


  edit(name: string) {

    const BASE_URL = "/api/series/";
    
    // this.seriesService.getSerieByName(nombre).subscribe(
    //   (elemento) => {
    //     // Procesa los valores del elemento aquí
    //     console.log('Nombre:', elemento.name);
    //     console.log('Tipo:', elemento.type);
    //     // ... otros valores del elemento
    //   },
    //   (error: any) => {
    //     console.error('Error al obtener la serie:', error);
    //   }
    // );

    this.seriesService.getSerieByName(name).subscribe({
      next: (element) => {
        var trueElement = Object.entries(element)
        // Inserta el contenido HTML en el elemento con id "modifyBookForm"
        console.log('Nombre:', element);
      },
    //     console.log('Tipo:', elemento.type);
      error: (err: any) => {
        console.error('Error al cargar el contenido HTML:', err);
      }
    });


    var resultsContainer = document.getElementById('modifyBookForm');


    // this.http.get('assets/W-EditFragment.html', { responseType: 'text' })
    // .subscribe({
    //   next: (html: string) => {
    //     // Inserta el contenido HTML en el elemento con id "modifyBookForm"
    //     const resultsContainer = document.getElementById('modifyBookForm');
    //     if (resultsContainer) {
    //       resultsContainer.innerHTML = html;
    //     }
    //   },
    //   error: (err: any) => {
    //     console.error('Error al cargar el contenido HTML:', err);
    //   }
    // });

  }

  

}
