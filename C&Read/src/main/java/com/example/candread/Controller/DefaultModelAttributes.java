package com.example.candread.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.candread.model.Element;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.security.web.csrf.CsrfToken;

@ControllerAdvice
public class DefaultModelAttributes {

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute(name = "username")
    public String getUserString(HttpServletRequest request) {

        String u = null;
        if (request.getUserPrincipal() != null) {
            String name = request.getUserPrincipal().getName();
            User user = userRepository.findByName(name).orElseThrow();
            u = user.getName();
        }
        return u;
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

    @ModelAttribute(name = "libros")
    public List<Element> listaLibros() {

        List<Element> listaLibros = devolverListadeTipo("LIBRO");
        return listaLibros;
    }

    @ModelAttribute(name = "peliculas")
    public List<Element> listaPeliculas() {
        List<Element> listaPeliculas = devolverListadeTipo("PELICULA");
        return listaPeliculas;
    }

    @ModelAttribute(name = "series")
    public List<Element> listaSeries() {
        List<Element> listaSeries = devolverListadeTipo("SERIE");
        return listaSeries;
    }

    @ModelAttribute(name = "TopLibros")
    public List<Element> topLibros() {

        List<Element> listaLibros = devolverListadeTipo("LIBRO");
        List<Element> listaTopLibros = new ArrayList<>();
        if (listaLibros.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                listaTopLibros.add(listaLibros.get(i));
            }
        }
        return listaTopLibros;
    }

    @ModelAttribute(name = "TopPeliculas")
    public List<Element> topPeliculas() {
        List<Element> listaPeliculas = devolverListadeTipo("PELICULA");
        List<Element> listaTopPeliculas = new ArrayList<>();
        if (listaPeliculas.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                listaTopPeliculas.add(listaPeliculas.get(i));
            }
        }
        return listaTopPeliculas;
    }

    @ModelAttribute(name = "TopSeries")
    public List<Element> topSeries() {
        List<Element> listaSeries = devolverListadeTipo("SERIE");
        List<Element> listaTopSeries = new ArrayList<>();
        if (listaSeries.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                listaTopSeries.add(listaSeries.get(i));
            }
        }
        return listaTopSeries;
    }

    public List<Element> devolverListadeTipo(String tipo) {

        List<Element> listaElementos = elementRepository.findAll();
        List<Element> lista = new ArrayList<>();
        for (Element i : listaElementos) {
            if (i.getType().equals(tipo)) {
                lista.add(i);
            }
        }
        return lista;
    }
}
