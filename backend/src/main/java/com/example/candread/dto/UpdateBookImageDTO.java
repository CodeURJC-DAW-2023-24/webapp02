package com.example.candread.dto;

import org.springframework.web.multipart.MultipartFile;


public class UpdateBookImageDTO {
    private MultipartFile imageFile;

    public UpdateBookImageDTO() {
    }

    public UpdateBookImageDTO(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }


    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
