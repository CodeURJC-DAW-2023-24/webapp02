import { Component, Input } from '@angular/core';
import { User } from '../models/user.model';
import { Element } from '../models/element.model';
import { UsersService } from '../services/user.service';
import { LoginService } from '../services/login.service';
import { UserDTO } from '../models/userDTO.model';
import { Observable } from 'rxjs';

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
    newProfileImage: string | undefined;
    newBannerImage: string | undefined;
    allUsers: Observable<User[]> | undefined; 
    allUsers2: User[] = [];

    constructor(private loginService: LoginService, private userService: UsersService) { }

    ngOnInit() {
        this.name = this.user?.name;
        this.loadLogoImage();
        this.loadBannerImage();
    }

    loadLogoImage() {
        if (this.user && this.user.id !== undefined) {
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
    loadBannerImage() {
        if (this.user && this.user.id !== undefined) {
            this.userService.getUserBannerImage(this.user?.id).subscribe((imageData) => {
                if (imageData) {
                    const blob = new Blob([imageData], { type: 'image/jpeg' });
                    this.bannerImage = URL.createObjectURL(blob);
                } else {
                    this.bannerImage = undefined;
                }
            });
        }

    }

    updateProfile() {
        this.user = {
            name: this.name!,
            roles: this.user?.roles!,
            password: this.user?.password!,
            listasDeElementos: this.user?.listasDeElementos!,
            id: this.user?.id
        };
        this.allUsers = this.userService.getUsers();
        const userdto: UserDTO = {};
        if(this.newProfileImage !== undefined && this.newProfileImage !== this.userImage){
            // this.userService.setUserImage(this.user?.id).subscribe({
            //     next: (response) => {

            //     }
            // })
        }
        if(this.newBannerImage !== undefined && this.newBannerImage !== this.bannerImage){
            //this.userService.setUserBannerImage(this.user?.id).subscribe()
        }

        this.userService.addOrUpdateUser(userdto, this.user).subscribe({
            next: (response: any) => {
                this.user!.id = response.id;
                if (this.user) {
                    //const existingUserIndex = this.allUsers!.findIndex(u => u.id === response.id);
                    this.allUsers2.findIndex
                }
            },
            error: (error) => {
                console.error('Error:', error);
            }
        })
    }
}
