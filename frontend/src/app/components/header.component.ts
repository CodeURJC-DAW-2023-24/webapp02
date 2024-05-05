import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';



@Component({
  selector: 'header-comp',
  templateUrl: '../Htmls/W-Header.component.html',
  styleUrl: '../Css/S-Header.css'
})
export class HeaderComponent {
  @Input() userUpdated: User | undefined;
  

  constructor(private router: Router){}





}
