package com.example.candread.dto;

import org.springframework.web.multipart.MultipartFile;


public class UpdateUserImagesDTO {

    private MultipartFile profileImage;
    private MultipartFile bannerImage;

    public UpdateUserImagesDTO() {
    }

    public UpdateUserImagesDTO(MultipartFile profileImage, MultipartFile bannerImage) {
        this.profileImage = profileImage;
        this.bannerImage = bannerImage;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public MultipartFile getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(MultipartFile bannerImage) {
        this.bannerImage = bannerImage;
    }

    
}
