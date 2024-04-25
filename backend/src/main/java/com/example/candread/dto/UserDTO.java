package com.example.candread.dto;

import java.util.List;
import java.util.Map;

import com.example.candread.model.User;

public class UserDTO {

    private String name;
    private List<String> roles;
    private String password;
    private Map<String, List<Long>> listasDeElementos;

    public UserDTO() {

    }
    public UserDTO(String name, List<String> roles, String password, Map<String, List<Long>> listasDeElementos) {
        this.name = name;
        this.roles = roles;
        this.password = password;
        this.listasDeElementos = listasDeElementos;
    }

    public UserDTO(User user) {
        this.name = user.getName();
        this.roles = user.getRoles();
        this.password = user.getPassword();
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
    public Map<String, List<Long>> getListasDeElementos() {
        return listasDeElementos;
    }
    public void setListasDeElementos(Map<String, List<Long>> listasDeElementos) {
        this.listasDeElementos = listasDeElementos;
    }

    

}
