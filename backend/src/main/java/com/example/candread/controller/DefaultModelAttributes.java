package com.example.candread.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.candread.model.Element;
import com.example.candread.model.User;
import com.example.candread.services.ElementService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.security.web.csrf.CsrfToken;

@ControllerAdvice
public class DefaultModelAttributes {

    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;

    @ModelAttribute(name = "user")
    public User getUser(HttpServletRequest request) {
        
        if (request.getUserPrincipal() != null) {
            String name = request.getUserPrincipal().getName();
            //User user = userRepository.findByName(name).orElseThrow();
            User user = userService.repoFindByName(name);
            return user;
        }
        return null;
    }

    @ModelAttribute(name = "admin")
    public Boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole("ADMIN");
    }

    @ModelAttribute
    public void addCsrfToken(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        if (token != null) {
            model.addAttribute("token", token.getToken());
        }
    }


    @ModelAttribute(name = "TopBooks")
    public List<Element> topBooks() {

        List<Element> booksList = getTypeList("LIBRO");
        List<Element> listTopBooks = new ArrayList<>();
        if (booksList.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                listTopBooks.add(booksList.get(i));
            }
        }
        return listTopBooks;
    }

    @ModelAttribute(name = "TopFilms")
    public List<Element> topFilms() {
        List<Element> filmsList = getTypeList("PELICULA");
        List<Element> topFilmsList = new ArrayList<>();
        if (filmsList.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                topFilmsList.add(filmsList.get(i));
            }
        }
        return topFilmsList;
    }

    @ModelAttribute(name = "TopSeries")
    public List<Element> topSeries() {
        List<Element> seriesList = getTypeList("SERIE");
        List<Element> topSeriesList = new ArrayList<>();
        if (seriesList.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                topSeriesList.add(seriesList.get(i));
            }
        }
        return topSeriesList;
    }

    public List<Element> getTypeList(String tipo) {

        //List<Element> listaElementos = elementRepository.findAll();
        List<Element> listaElementos = elementService.repoFindAll();
        List<Element> lista = new ArrayList<>();
        for (Element i : listaElementos) {
            if (i.getType().equals(tipo)) {
                lista.add(i);
            }
        }
        return lista;
    }
}
