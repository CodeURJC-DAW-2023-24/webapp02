import { Component, EventEmitter, Input, Output } from '@angular/core';
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
    //@Output() userToSend: User | undefined;
    @Output()
    userToSend: EventEmitter<User> = new EventEmitter();


    userImage: string | undefined;
    bannerImage: string | undefined;
    name: string | undefined;
    newProfileImage: File | undefined;
    newBannerImage: File | undefined;
    allUsers: Observable<User[]> | undefined;
    allUsers2: User[] = [];
    tempImage: File | undefined;
    userUpdateVar: User | undefined;

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
                    this.user =JSON.parse(localStorage.getItem('currentUser')!) as User;
                    this.user!.imageURL = URL.createObjectURL(blob);
                    
                    this.userToSend.emit(this.user);
                } else {
                    this.userImage = undefined;
                }
            });
        }
    }
    loadBannerImage() {
        if (this.user && this.user.id !== undefined) {
            this.userService.getUserBannerImage(this.user?.id).subscribe((imageData) => {
                if (imageData) {
                    const blob = new Blob([imageData], { type: 'image/jpeg' });
                    this.bannerImage = URL.createObjectURL(blob);
                    this.user = JSON.parse(localStorage.getItem('currentUser')!) as User;
                    this.user.bannerImageURL = URL.createObjectURL(blob);

                    // this.userImage = URL.createObjectURL(blob);
                    // this.user =JSON.parse(localStorage.getItem('currentUser')!) as User;
                    // this.user!.imageURL = URL.createObjectURL(blob);
                } else {
                    this.bannerImage = undefined;
                }
            });
        }
    }

    setImages(event: any) {
        this.newProfileImage = event.target.files[0] as File;
    }
    setBanners(event: any) {
        this.newBannerImage = event.target.files[0] as File;
    }

    updateProfile() {
        if (this.newProfileImage !== undefined) {
            console.log("Me has mandado una nueva imagen para cambiarla");
            this.userService.setUserImage(this.user!.id!, this.newProfileImage).subscribe({
                next: (response: any) => {

                    this.userUpdateVar = response as User;
                    const urlNewProfileImg = URL.createObjectURL(this.newProfileImage!);
				    this.userUpdateVar.imageURL = urlNewProfileImg; 

                    this.userService.updateCurrentUser(this.userUpdateVar);
                    this.loadLogoImage();
                    this.loadBannerImage();

                }
            });
        }
        if (this.newBannerImage !== undefined) {
            this.userService.setUserBannerImage(this.user!.id!, this.newBannerImage).subscribe({
                next: (response: any) => {
                    //this.userService.updateCurrentUser(response);

                    this.userUpdateVar = response as User;
                    const urlNewBannerImg = URL.createObjectURL(this.newBannerImage!);
				    this.userUpdateVar.bannerImageURL = urlNewBannerImg; 

                    this.userService.updateCurrentUser(this.userUpdateVar);
                    this.loadBannerImage();
                    this.loadLogoImage();
                }
            });
        }
        const userdto: UserDTO = {
            name: this.name!,
        };
        this.allUsers = this.userService.getUsers();
        this.userService.addOrUpdateUser(userdto, this.user!).subscribe({
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
