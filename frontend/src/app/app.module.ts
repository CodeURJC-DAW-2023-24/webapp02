import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './components/app.component';
import { routing } from './app.routing';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { UserDataComponent } from './components/userData.component';
import { HeaderComponent } from './components/header.component';
import { LibraryComponent } from './components/library.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, HeaderComponent, UserDataComponent, LibraryComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

