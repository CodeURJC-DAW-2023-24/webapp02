import { Component, Input } from '@angular/core';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { UsersService } from '../services/user.service';
import { LoginService } from '../services/login.service';

@Component({
    selector: 'app-banner',
    templateUrl: '../Htmls/W-Banner.component.html',
    styleUrl: '../Css/S-Banner.css'
})
export class BannerComponent {
    @Input() user: User | undefined;
    userImage: string | undefined;
    bannerImage: string | undefined;
    name: string | undefined;

    constructor(private loginService: LoginService, private userService: UsersService) { }

    ngOnInit() {
        this.name=this.user?.name;
        this.loadLogoImage();
        this.loadBannerImage();
    }

    loadLogoImage() {
        if(this.user && this.user.id !== undefined){
            this.userService.getUserImage(this.user?.id).subscribe((imageData) => {
                if (imageData) {
                    const blob = new Blob([imageData], { type: 'image/jpeg' });
                    this.userImage = URL.createObjectURL(blob);
                } else {
                    this.userImage = undefined;
                }
            });
        }
    } //LoadImage end
    loadBannerImage(){
        if(this.user && this.user.id !== undefined){
            this.userService.getUserBannerImage(this.user?.id).subscribe((imageData) =>{
                if(imageData){
                    const blob = new Blob([imageData], {type: 'image/jpeg'});
                    this.bannerImage = URL.createObjectURL(blob);
                } else {
                    this.bannerImage = undefined;
                }
            });
        }

    }
    changeName(entryname:string){
        this.name = entryname;
    }
}
