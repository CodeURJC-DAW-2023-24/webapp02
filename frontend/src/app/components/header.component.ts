import { Component } from '@angular/core';
import { Router } from '@angular/router';



@Component({
  selector: 'header-comp',
  templateUrl: '../Htmls/W-Header.component.html',
  styleUrl: '../Css/S-Header.css'
})
export class HeaderComponent {
  constructor(private router: Router){}



}
