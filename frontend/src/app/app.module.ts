import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { UserDataComponent } from './components/userData.component';
import { HeaderComponent } from './components/header.component';
import { CarouselComponent } from './components/carousel.component';
import { NewsComponent } from './components/news.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, HeaderComponent, UserDataComponent, CarouselComponent, NewsComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

