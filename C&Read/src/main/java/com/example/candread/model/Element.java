package com.example.candread.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Element {

    public enum Types{
        PELICULA, SERIE, LIBRO
    }

    public enum Seasons{
        OTOÑO, INVIERNO, PRIMAVERA, VERANO
    }
    public enum States{
        COMPLETO, EN_EMISION, POR_ESTRENARSE
    }
    public enum Countries{
        ESPAÑA, ESTADOSUNIDOS, JAPON, CHINA, REINOUNIDO
    }
    public enum Genres{
        ACCION, TERROR, AVENTURA, MISTERIO, ROMANCE, CIENCIAFICCION, DRAMA, INFANTIL, COMEDIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String author;
    private String image;

    //private reseñas


    //private genero - lista
    private List<Genres> genres;

    private Types type;
    private Seasons season;
    private States state;
    private Countries country;


    //CONTRUCTOR DEL ELEMENT:



    //GETTERS Y SETTERS

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
