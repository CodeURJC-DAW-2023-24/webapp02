package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.model.Element.Genres;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.services.ElementService;
import com.mysql.cj.jdbc.Blob;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


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
    

    @PostMapping("/add")
    public String addElement(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("author") String author, 
    @RequestParam("type") String type, @RequestParam("season") String season, @RequestParam("state") String state, 
    @RequestParam("country") String country, @RequestParam("genres") List<String> genres,  @RequestParam("image") MultipartFile image,
    Model model, HttpServletRequest request) {
    

        try {
 
            Element newElement = new Element(name,description,author,type,season,state,country,genres);
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

}
