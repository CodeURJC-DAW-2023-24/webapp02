import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './components/app.component';
import { routing } from './app.routing';
import { MainComponent } from './components/components/main.component';
import { LoginComponent } from './components/components/login.component';
import { UserDataComponent } from './components/components/userData.component';
import { HeaderComponent } from './components/components/header.component';
import { LibraryComponent } from './components/library.component';
import { CarouselComponent } from './components/carousel.component';
import { NewsComponent } from './components/news.component';
import { CardsComponent } from './components/card.component';
import { TopsComponent } from './components/top.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, HeaderComponent, UserDataComponent, LibraryComponent, CarouselComponent, NewsComponent, CardsComponent, TopsComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

