package com.example.candread.model;

import java.util.ArrayList;
import java.util.List;

import java.sql.Blob;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

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
        ESPAÑA, ESTADOS_UNIDOS, JAPON, CHINA, REINO_UNIDO, COREA
    }

    public enum Genres {
        ACCION, TERROR, AVENTURA, MISTERIO, ROMANCE, CIENCIAFICCION, DRAMA, INFANTIL, COMEDIA, FANTASIA, SOBRENATURAL, NOVELA, JUVENIL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column (length = 5000) //Más espacio para las descripciones
    private String description;

    private String author;
    
    // Imagenes:
    private String image;
    @Lob 
    private Blob imageFile;

    private String type;
    private String season;
    private String state;
    private String country;
    @Transient
    private String base64Image;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> genres;

    @OneToMany (mappedBy = "elementLinked", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // CONSTRUCTOR DEL ELEMENT:

    public Element(){

    }

    public Element(String nombre, String descripcion, String autor, String imagen1,
            String type1, String temporada, String estado, String pais, List<String> generosEjemplo){ //List<Review> reseñas) {
        this.name = nombre;
        this.description = descripcion;
        this.author = autor;
        this.image = imagen1;
        this.type = type1;
        this.season = temporada;
        this.state = estado;
        this.country = pais;
        this.genres = generosEjemplo;
        //this.reviews = reseñas;
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

    public String getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type.name();
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(Seasons season) {
        this.season = season.name();
    }

    public String getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state.name();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(Countries country) {
        this.country = country.name();
    }

    public List<String> getGeneros() {
        return genres;
    }

    public void setGeneros(List<String> generos) {
        this.genres = generos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }


    
    
}