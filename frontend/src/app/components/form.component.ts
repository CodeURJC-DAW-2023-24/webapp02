import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Element } from '../models/element.model';
import { Observable } from 'rxjs';



@Component({
    selector: 'formComponent',
    templateUrl: '../Htmls/W-Form.component.html',
    styleUrl: '../Css/S-Header.css'
})
export  class FormComponent  { 
    
    // observer! : Observable<Element>

    @Input()
    element!: Element;
    


    name: string = "";
    description: string =  "";
    year: number =  0;
    type: string =  "";
    season: string =  "";
    state: string =  "";
    author: string =  ""
    country: string =  "";

    
    //genres : string[] = this.element.genres

     constructor()  {
        this.init()
    }

    public async  init () {

        // this.observer.subscribe((response: any) => {
        //     this.element = response;
            // this.name = this.element.name
            // this.description = this.element.description
            // this.author = this.element.name
            // this.type= this.element.type
            // this.state = this.element.state
            // this.season = this.element.season
            // this.country = this.element.country
            // this.year = this.element.year
      
        //     })

            this.name = this.element.name
            this.description = this.element.description
            this.author = this.element.name
            this.type= this.element.type
            this.state = this.element.state
            this.season = this.element.season
            this.country = this.element.country
            this.year = this.element.year
    }
    // name = elemnt.name

}