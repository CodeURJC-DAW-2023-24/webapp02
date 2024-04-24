import { Component, OnInit } from '@angular/core';
import { ElementsService } from '../services/element.service';
import { NewsService } from '../services/new.service';
import { Element } from '../models/element.model';
import { New } from '../models/new.model';

@Component({
  selector: 'main',
  templateUrl: '../Htmls/W-Main.component.html',
  styleUrls: ['../Css/S-Main.css']
})
export class MainComponent implements OnInit {

  elements: Element[] = [];
  news: New[] = [];

  constructor(private elementsService: ElementsService, private newsService: NewsService) { }

  ngOnInit(): void {
    this.getAllElements();
    this.get5RecentNews();
  }

  getAllElements(): void {
    this.elementsService.getAllElements().subscribe({
      next: (response: Element[]) => {
        this.elements = response;
        // Otros procesamientos de la respuesta, si es necesario
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  get5RecentNews(): void {
    this.newsService.get5RecentNews().subscribe({
      next: (news: New[]) => {
        this.news = news;
      },
      error: (error) => {
        console.error('Error al obtener las noticias:', error);
      }
    });
  }

}
