import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { MainComponent } from './main.component';
import { LoginComponent } from './login.component';
import { ProfileComponent } from './profile.component';

@NgModule({
  declarations: [AppComponent, MainComponent, LoginComponent, ProfileComponent],
  imports: [BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }

