package com.example.candread.dto;

import java.util.List;

import com.example.candread.model.User;

public class UserDTO {
    private Long id;
    private String name;
    private List<String> roles;
    private String password;

    public UserDTO() {

    }

    public UserDTO(Long id, String name, List<String> roles, String password) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.password = password;
    }

    public UserDTO(User user) {

        this.id = user.getId();
        this.name = user.getName();
        this.roles = user.getRoles();
        this.password = user.getPassword();
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    

    

}
