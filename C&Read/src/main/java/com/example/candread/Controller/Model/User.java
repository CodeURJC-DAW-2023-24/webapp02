package com.example.candread.Controller.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
public class User {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String rol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String password1;

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    

    public String getRol() {
        return rol;
    }

    public void setRole(String rol) {
        this.rol = rol;
    }

    
    
}
