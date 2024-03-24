package com.example.candread.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.candread.model.Element.Basico;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.GenerationType;

@Entity
public class User {

    //ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Basico.class)
    private Long id;

    @JsonView(Basico.class)
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonView(Basico.class)
	private List<String> roles;

    @JsonView(Basico.class)
    private String password;

    @OneToMany (mappedBy = "userLinked", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany (mappedBy = "users", cascade = CascadeType.ALL)
    private List<Element> elements = new ArrayList<>();

    //@ManyToMany (mappedBy = "userFavourited", cascade = CascadeType.ALL)
    //@ElementCollection(fetch = FetchType.EAGER)  <-FUNCIONA
  
    @ManyToMany (mappedBy = "usersFavourited", cascade = CascadeType.ALL)
    private List<Element> favourites = new ArrayList<>();

    @Lob 
    private Blob profileImage;

    @Lob
    private Blob bannerImage;

    @Transient
    private String base64ProfileImage;

    @Transient
    private String base64BannerImage;

    
    @ElementCollection
    @CollectionTable(name="user_elements_lists", joinColumns = @JoinColumn(name="user_id"))
    @MapKeyColumn(name = "list_name")
    @Column(name = "element_id")
    private Map<String, List<Long>> listasDeElementos;


    //CONTROLLERS
    public User() {
    }

    public User(String name, String password, String... roles) {
		this.name = name;
		this.password = password;
        this.roles = List.of(roles);
	}

    //GETTERS&SETTERS
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

    public Blob getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Blob profileImage) {
        this.profileImage = profileImage;
    }

    public Blob getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(Blob bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBase64ProfileImage() {
        return base64ProfileImage;
    }

    public void setBase64ProfileImage(String base64ProfileImage) {
        this.base64ProfileImage = base64ProfileImage;
    }

    public String getBase64BannerImage() {
        return base64BannerImage;
    }

    public void setBase64BannerImage(String base64BannerImage) {
        this.base64BannerImage = base64BannerImage;
    }

    public Map<String, List<Long>> getListasDeElementos() {
        return listasDeElementos;
    }

    public void setListasDeElementos(Map<String, List<Long>> listasDeElementos) {
        this.listasDeElementos = listasDeElementos;
    }

    

    

}
