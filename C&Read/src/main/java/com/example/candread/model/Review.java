package com.example.candread.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    private String title;
    private String description;
    private int points;

    

    
    public Review(String title, String description, int points) {
        this.title = title;
        this.description = description;
        this.points = points;
    }

    /*public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }*/
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    
    
}
