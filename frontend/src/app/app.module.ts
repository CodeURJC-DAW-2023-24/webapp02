import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './components/app.component';
import { routing } from './app.routing';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { ProfileComponent } from './components/profile.component';
import { UserDataComponent } from './components/userData.component';
import { HeaderComponent } from './components/header.component';
import { LibraryComponent } from './components/library.component';
import { CarouselComponent } from './components/carousel.component';
import { NewsComponent } from './components/news.component';
import { CardsComponent } from './components/card.component';
import { TopsComponent } from './components/top.component';
import { StatsComponent } from './components/stats.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, ProfileComponent, HeaderComponent, UserDataComponent, LibraryComponent, CarouselComponent, NewsComponent, CardsComponent, TopsComponent, StatsComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

