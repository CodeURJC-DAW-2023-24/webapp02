package com.example.candread.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

    private String password;

    //Apartado para asociar el usuario a las reviews que tiene.
    @OneToMany (mappedBy = "userLinked", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    //Apartado para asociar el usuario a las reviews que tiene.
    @ManyToMany (mappedBy = "users", cascade = CascadeType.ALL)
    private List<Element> elements = new ArrayList<>();

    //@ManyToMany (mappedBy = "userFavourited", cascade = CascadeType.ALL)
    //@ElementCollection(fetch = FetchType.EAGER)  <-FUNCIONA
    @ManyToMany (mappedBy = "usersFavourited", cascade = CascadeType.ALL)
    private List<Element> favourites = new ArrayList<>();

    public User() {
    }

    public User(String name, String password, String... roles) {
		this.name = name;
		this.password = password;
        this.roles = List.of(roles);
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Element> favourites) {
        this.favourites = favourites;
    }

    

    

}
