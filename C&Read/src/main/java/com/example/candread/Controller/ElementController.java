package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.candread.model.Element;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.services.ElementService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/SingleElement")
public class ElementController {
    
    @Autowired
    private ElementRepository elementRepository;

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
    

}
