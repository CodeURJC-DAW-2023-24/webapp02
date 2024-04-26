import { Component, Input } from '@angular/core';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { UsersService } from '../services/user.service';

@Component({
    selector: 'app-banner',
    templateUrl: '../Htmls/W-Banner.component.html',
    styleUrl: '../Css/S-Banner.css'
})
export class BannerComponent {
    @Input() user: User | undefined;
}
