package com.example.candread.dto;

import jakarta.persistence.Entity;

@Entity
public class ReviewDTO {

    private String body;
    private int rating;

    public ReviewDTO(String cuerpo, int puntuacion) {
        this.body = cuerpo;
        this.rating = puntuacion;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
