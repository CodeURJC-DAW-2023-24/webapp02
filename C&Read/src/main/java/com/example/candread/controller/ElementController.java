package com.example.candread.controller;

import java.io.IOException;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.candread.model.Element;

import com.example.candread.model.Review;

import com.example.candread.model.User;

import com.example.candread.repositories.ElementRepository;
import com.example.candread.services.ElementService;
import com.example.candread.services.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/SingleElement")
public class ElementController {

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private ElementService elementService;

    @Autowired
    private UserService userService;
    
     List<String> baseGenres = new ArrayList<>(Arrays.asList(
            "ACCION", "AVENTURA", "TERROR",  "MISTERIO", "ROMANCE", "CIENCIAFICCION", "DRAMA",
            "INFANTIL", "COMEDIA", "FANTASIA", "SOBRENATURAL", "NOVELA", "JUVENIL"
        ));

    @GetMapping("/{id}")
    public String getSingleElement(@PathVariable("id") Long id, Model model) throws SQLException, IOException {
        userService.fullSet64Image();
        if (id != null) {
            Optional<Element> optionalElement = elementRepository.findById(id);
            if (optionalElement.isPresent()) {
                Element serie = optionalElement.get();
                List<Review> reviews = serie.getReviews();
                Map<Review, String> reviewsConUsuarios = new HashMap<>();
                int totalRating = 0;
                for (Review r : reviews) {
                    String userName = (r.getUserLinked() != null) ? r.getUserLinked().getName() : "ANONYMOUS";
                    reviewsConUsuarios.put(r, userName);
                    totalRating += r.getRating();
                }
                double averageRating = 0;
                if (totalRating != 0) {
                    averageRating = (double) totalRating / reviews.size();
                }
                model.addAttribute("averageRating", averageRating);
                model.addAttribute("serie", serie);
                model.addAttribute("reviewsConUsuarios", reviewsConUsuarios);

                elementService.fullSet64Image();
                return "W-SingleElement";
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/{id}/addelement")
    public String addElement(@PathVariable("id") Long id, Model model, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        if (id != null) {
            Optional<Element> optionalElement = elementRepository.findById(id);
            Element newElement = optionalElement.orElseThrow();

            User user = (User) model.getAttribute("user");
            long elementId = newElement.getId();

            List<User> userList = new ArrayList<>();
            userList = newElement.getUsers();
            if (userList.contains(user)) {
                //
                return "redirect:/SingleElement/" + elementId;
            } else {
                newElement.getUsers().add(user);
                // user.getElements().clear();
                // user.setElements(newList);
                elementRepository.save(newElement);
                return "redirect:/SingleElement/" + elementId;
            }
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/{id}/favourite")
    public String addFavourite(@PathVariable("id") Long id, Model model, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        if (id != null) {
            Optional<Element> optionalElement = elementRepository.findById(id);
            Element newElement = optionalElement.orElseThrow();

            User user = (User) model.getAttribute("user");
            long elementId = newElement.getId();

            List<User> userList = new ArrayList<>();
            userList = newElement.getUsersFavourited();
            if (userList.contains(user)) {
                //
                return "redirect:/SingleElement/" + elementId;
            } else {
                newElement.getUsersFavourited().add(user);
                elementRepository.save(newElement);
                return "redirect:/SingleElement/" + elementId;
            }
        } else {
            return "redirect:/error";
        }

    }

    @PostMapping("/add")
    public String addElement(@RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "description", required = false) String description,
    @RequestParam(value = "author", required = false) String author,
    @RequestParam(value = "type", required = false) String type,
    @RequestParam(value = "season", required = false) String season,
    @RequestParam(value = "state", required = false) String state,
    @RequestParam(value = "country", required = false) String country,
    @RequestParam(value = "genres", required = false) String genres,
    @RequestParam(value = "image", required = false) MultipartFile image,
    @RequestParam(value = "years", required = false) Integer years,
    Model model, HttpServletRequest request) {

        try {

            String genresFormated = genres.toUpperCase().strip();
            List<String> genreList = Arrays.stream(genresFormated.split(","))
                                       .map(String::trim)
                                       .collect(Collectors.toList());            
            Element newElement = new Element(name, description, author, type, season, state, country, genreList, years);
            if (genres != null) {
                for (String genre : genreList) {
                    if (!this.baseGenres.contains(genre.strip())) {
                        this.baseGenres.add(genre);
                    }
                }
            }
            byte[] imageData = image.getBytes();
            SerialBlob blob = new SerialBlob(imageData);
            newElement.setImageFile(blob);
            elementRepository.save(newElement);


        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la noticia.");
        }

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "redirect:/Admin";
    }



    @PostMapping("/edit")
    public String editElement(@RequestParam("nameSearch") String nameSearch, Model model, HttpServletRequest request) {
    try {
        List<Element> optionalElements = elementRepository.findByName(nameSearch);

        if (!optionalElements.isEmpty()) {
            if (optionalElements.size() == 1) {
                // Si solo hay un elemento encontrado, proceder con la edición como antes
                Element element = optionalElements.get(0);
                List<String> genres = element.getGeneros();
                model.addAttribute("elemen", element);
                model.addAttribute("genres", genres);
                return "W-EditFragment";
            } else {

                List<String> types = new ArrayList<>();
                for (int i = 0; i < optionalElements.size(); i++) {
                    Element element = optionalElements.get(i);
                    String elementType = element.getType();
                    types.add(elementType);                    
                } 
                // Si hay más de un elemento encontrado, mostrar una página para que el usuario elija el tipo de elemento
                model.addAttribute("name", nameSearch);
                model.addAttribute("types", types);

                return "W-ChooseElementTypePage"; // Página para que el usuario elija el tipo de elemento
            }
        } else {
            model.addAttribute("errorOccurred", true);
            return "W-ModifyFragment"; // Devuelve la página de modificación
        }
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al buscar el elemento.");
       
    }

    if (request.getAttribute("_csrf") != null) {
        model.addAttribute("token", request.getAttribute("_csrf").toString());
    }

    return ""; 
}

    @PostMapping("/edit/type")
    public String typeElement(@RequestParam("type") String type, @RequestParam("name") String name, Model model, HttpServletRequest request) {
    try {
        List<Element> elements = elementRepository.findByName(name);

        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String elementType = element.getType();
           if ( elementType.equals(type)) {
                Element choosedElement = element;
                List<String> genres = choosedElement.getGeneros();
                model.addAttribute("elemen", choosedElement);
                model.addAttribute("genres", genres);


                return "W-EditFragment";
            }
        } 
        return "";
        
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al buscar el elemento.");
    }

    if (request.getAttribute("_csrf") != null) {
        model.addAttribute("token", request.getAttribute("_csrf").toString());
    }

    return ""; // Retornar la página de error o redirigir a otra página según tu lógica
}


    @PostMapping("/edit/add")
    public String changeElement(@RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "description", required = false) String description,
    @RequestParam(value = "author", required = false) String author,
    @RequestParam(value = "type", required = false) String type,
    @RequestParam(value = "season", required = false) String season,
    @RequestParam(value = "state", required = false) String state,
    @RequestParam(value = "country", required = false) String country,
    @RequestParam(value = "genres", required = false) List<String> genres,
    @RequestParam(value = "image", required = false) MultipartFile image,
    @RequestParam(value = "years", required = false) Integer years,
    @RequestParam(value = "elementId", required = false) Long id,
    Model model, HttpServletRequest request) {

        try {
            Optional<Element> optionalElement = elementRepository.findById(id);
            Element element = optionalElement.orElseThrow();
            element.setName(name);
            element.setDescription(description);
            element.setAuthor(author);
            if (type.equals("LIBRO")) {
                element.setType(Element.Types.LIBRO);
            }
            else if (type.equals("SERIE")) {
                element.setType(Element.Types.SERIE);
            }
            else if (type.equals("PELICULA")) {
                element.setType(Element.Types.PELICULA);
            }

            if (state.equals("COMPLETO")) {
                element.setState(Element.States.COMPLETO);
            }
            else if (state.equals("EN_EMISION")) {
                element.setState(Element.States.EN_EMISION);
            }
            else if (state.equals("POR_ESTRENARSE")) {
                element.setState(Element.States.POR_ESTRENARSE);
            }


            if (country.toUpperCase().equals("JAPÓN") || country.toUpperCase().equals("JAPON")) {
                element.setCountry(Element.Countries.JAPON);
            }
            else if (country.toUpperCase().equals("CHINA")) {
                element.setCountry(Element.Countries.CHINA);
            }
            else if (country.toUpperCase().equals("COREA")) {
                element.setCountry(Element.Countries.COREA);
            }

            if (country.toUpperCase().equals("ESTADOS_UNIDOS")) {
                element.setCountry(Element.Countries.ESTADOS_UNIDOS);
            }
            else if (country.toUpperCase().equals("ESPAÑA")) {
                element.setCountry(Element.Countries.ESPAÑA);
            }
            else if (country.toUpperCase().equals("REINO_UNIDO")) {
                element.setCountry(Element.Countries.REINO_UNIDO);
            }
            
        
            List<String> formattedGenres = new ArrayList<>();
            for (String genre : genres) {
                String formattedGenre = genre.replaceAll("\\[|\\]", ""); 
                formattedGenres.add(formattedGenre); 
            }
            element.setGeneros(formattedGenres);
            byte[] imageData = image.getBytes();
            SerialBlob blob = new SerialBlob(imageData);
            element.setImageFile(blob);
            elementRepository.save(element);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la noticia.");
        }

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "redirect:/Admin";
    }





}
