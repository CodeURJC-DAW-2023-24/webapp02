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

  userPrueba: User | undefined;
  userImage: string | undefined;
  bannerImage: string | undefined;
  name: string | undefined;
  originalName: string | undefined;
  newProfileImage: File | undefined;
  newBannerImage: File | undefined;
  allUsers: Observable<User[]> | undefined;
  allUsers2: User[] = [];
  tempImage: File | undefined;
  userUpdateVar: User | undefined;

  constructor(private loginService: LoginService, private userService: UsersService) { }

  ngOnInit() {
    this.name = this.user?.name;
    this.originalName = this.user?.name;
    this.loadLogoImage();
    this.loadBannerImage();
  }

  loadLogoImage() {
    if (this.user && this.user.id !== undefined) {
      this.user = JSON.parse(localStorage.getItem('currentUser')!) as User;
      this.newProfileImage = undefined;
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
          this.newBannerImage = undefined;

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

    if (this.name !== this.originalName) {
      const userdto: UserDTO = {
        name: this.name,
        profileImageUrl: this.user?.imageURL,
        bannerImageUrl: this.user?.bannerImageURL
      };
      this.userService.addOrUpdateUser(userdto, this.user!).subscribe({
        next: (response: any) => {
          this.userUpdateVar = response as User;
          this.loginService.updateCurrentUser(this.userUpdateVar);
          this.userService.updateCurrentUser(this.userUpdateVar);
          this.user= JSON.parse(localStorage.getItem('currentUser')!) as User;
          //this.name = this.user.name;
          this.user!.id = response.id;
          // if (this.user) {
          //   this.allUsers2.findIndex;
          // }
        },
        error: (error) => {
          console.error('Error:', error);
        }
      })
    }

    if (this.newProfileImage !== undefined) {
      console.log("Me has mandado una nueva imagen para cambiarla");
      const userdto: UserDTO = {
        profileImageUrl: URL.createObjectURL(this.newProfileImage!),
        bannerImageUrl: this.user?.bannerImageURL,
        profileImage: this.newProfileImage
      };

      this.userService.setUserImage(this.user!.id!, userdto).subscribe({
        next: (response: any) => {
          this.userUpdateVar = response as User;
          const urlNewProfileImg = URL.createObjectURL(this.newProfileImage!);
          this.userUpdateVar.imageURL = urlNewProfileImg;

          this.userService.updateCurrentUser(this.userUpdateVar);

          this.loginService.updateCurrentUser(this.userUpdateVar);
          this.loadLogoImage();
          this.loadBannerImage();

        }
      });
    }
    if (this.newBannerImage !== undefined) {
      const userdto: UserDTO = {
        profileImageUrl: this.user?.imageURL,
        bannerImageUrl: URL.createObjectURL(this.newBannerImage!),
        bannerImage: this.newBannerImage
      };
      this.userService.setUserBannerImage(this.user!.id!, userdto).subscribe({
        next: (response: any) => {
          this.userUpdateVar = response as User;
          const urlNewBannerImg = URL.createObjectURL(this.newBannerImage!);
          this.userUpdateVar.bannerImageURL = urlNewBannerImg;

          this.userService.updateCurrentUser(this.userUpdateVar);
          this.loadBannerImage();
          this.loadLogoImage();
        }
      });
    }
  }
}
