package com.example.candread.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Element {

    public enum Types {
        PELICULA, SERIE, LIBRO
    }

    public enum Seasons {
        OTOÑO, INVIERNO, PRIMAVERA, VERANO
    }

    public enum States {
        COMPLETO, EN_EMISION, POR_ESTRENARSE
    }

    public enum Countries {
        ESPAÑA, ESTADOSUNIDOS, JAPON, CHINA, REINOUNIDO
    }

    public enum Genres {
        ACCION, TERROR, AVENTURA, MISTERIO, ROMANCE, CIENCIAFICCION, DRAMA, INFANTIL, COMEDIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String author;

    // Imagenes:
    private String image;

    // private reseñas
    @OneToMany
    private List<Review> reviews;

    // private genero - lista
    private List<Genres> genres;

    private Types type;
    private Seasons season;
    private States state;
    private Countries country;

    // CONSTRUCTOR DEL ELEMENT:

    public Element(String name, String description, String author,
            List<Review> reviews, Types type, Seasons season, States state,
            Countries country, List<Genres> genres1) {
        this.name = name;
        this.description = description;
        this.author = author;

        // this.image

        // this.review
        // this.reviews = List.of(reviews);

        // this.genres= List.of(genres1);

        this.reviews = new ArrayList<>();
        this.reviews.addAll(reviews);

        this.genres = new ArrayList<>();
        this.genres.addAll(genres1);

        this.type = type;
        this.season = season;
        this.state = state;
        this.country = country;
    }

    // GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public Seasons getSeason() {
        return season;
    }

    public void setSeason(Seasons season) {
        this.season = season;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public Countries getCountry() {
        return country;
    }

    public void setCountry(Countries country) {
        this.country = country;
    }

    public List<Genres> getGeneros() {
        return genres;
    }

    public void setGeneros(List<Genres> generos) {
        this.genres = generos;
    }

}
