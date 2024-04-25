import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppComponent } from './components/app.component';
import { routing } from './app.routing';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { UserDataComponent } from './components/userData.component';
import { HeaderComponent } from './components/header.component';
import { LibraryComponent } from './components/library.component';
import { CarouselComponent } from './components/carousel.component';
import { NewsComponent } from './components/news.component';
import { CardsComponent } from './components/card.component';
import { TopsComponent } from './components/top.component';
import { FilterComponent } from './components/filters.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, HeaderComponent, UserDataComponent, LibraryComponent,
    CarouselComponent, NewsComponent, CardsComponent, TopsComponent, FilterComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

