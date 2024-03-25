package com.example.candread.dto;

import java.util.List;

import com.example.candread.model.Element;

public class ElementDTO {
    private String name;
    private String description;
    private String author;
    private int year;
    private String type;
    private String season;
    private String state;
    private String country;
    private List<String> genres;

    public ElementDTO() {

    }

    public ElementDTO(String name, String description, String author, int year, String type, String season, String state, String country, List<String> genres) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.year = year;
        this.type = type;
        this.season = season;
        this.state = state;
        this.country = country;
        this.genres = genres;
    }

    public ElementDTO(Element element) {
        this.name = element.getName();
        this.description = element.getDescription();
        this.author = element.getAuthor();
        this.year = element.getYear();
        this.type = element.getType();
        this.season = element.getSeason();
        this.state = element.getState();
        this.country = element.getCountry();
        this.genres = element.getGeneros();
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

}
