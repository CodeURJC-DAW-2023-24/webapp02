package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.candread.model.Element;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.ElementService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/SingleElement")
public class ElementController {
    
    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElementService elementService;

    @GetMapping("/{id}")
    public String getSingleElement(@PathVariable("id") Long id, Model model) throws SQLException, IOException {

        // Obtener la serie correspondiente al ID proporcionado
        Optional<Element> optionalElement = elementRepository.findById(id);
        
        if (optionalElement.isPresent()) {
            Element serie = optionalElement.get();
            model.addAttribute("serie", serie);
            elementService.fullSet64Image();
            return "W-SingleElement"; 
        } else {
            // Manejar el caso en el que no se encuentra la serie
            String nu = null;
            return "redirect:/error"; 
        }
    }

    @PostMapping("/{id}/favourite")
    public String addFavourite(@PathVariable("id") Long id, Model model, HttpServletRequest request)throws SQLException, IOException{

        Optional<Element> optionalElement = elementRepository.findById(id);
        Element newElement = optionalElement.orElseThrow();

        User user = (User) model.getAttribute("user");
        long elementId = newElement.getId();

        /*List<Element> newList = new ArrayList<>();
        newList = user.getElements();
        newList.add( newElement); */

        List<User> userList = new ArrayList<>();
        userList = newElement.getUsers();
        if(userList.contains(user)){
            //Metodo para notificar que ya existe
            return "redirect:/SingleElement/"+elementId;
        } else {
            newElement.getUsers().add(user);
            //user.getElements().clear();
            //user.setElements(newList);
            elementRepository.save(newElement);
            return "redirect:/SingleElement/"+elementId;
        }
        /* 
        //Element serie = optionalElement.get();
        model.addAttribute("serie", newElement);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }*/
        
    }
    

}
