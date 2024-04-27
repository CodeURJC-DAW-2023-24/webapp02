package com.example.candread.dto;

public class ReviewDTO {

    private String body;
    private int rating;
    private long userId;
    private long elementId;

    public ReviewDTO(String cuerpo, int puntuacion, long userId, long elementId) {
        this.body = cuerpo;
        this.rating = puntuacion;
        this.userId = userId;
        this.elementId = elementId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }

    
}
