package com.example.candread.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @JsonView(Basico.class)
    private Long id;

    @Column (length = 5000)
    // @JsonView(Basico.class)
    private String body;

    // @JsonView(Basico.class)
    private int rating;

    @ManyToOne 
    @JoinColumn(name = "element_ID")
    @JsonIgnore
    private Element elementLinked;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    @JsonIgnore
    private User userLinked;

    public Review(){

    }

    public Review(String cuerpo, int puntuacion) {
        this.body = cuerpo;
        this.rating = puntuacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Element getElementLinked() {
        return elementLinked;
    }

    public void setElementLinked(Element elementLinked) {
        this.elementLinked = elementLinked;
    }

    public User getUserLinked() {
        return userLinked;
    }

    public void setUserLinked(User userLinked) {
        this.userLinked = userLinked;
    }

}
