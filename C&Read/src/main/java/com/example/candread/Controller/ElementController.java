package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.candread.model.Element;
import com.example.candread.model.Review;
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

        Optional<Element> optionalElement = elementRepository.findById(id);

        if (optionalElement.isPresent()) {
            Element serie = optionalElement.get();
            List<Review> reviews = serie.getReviews();
            Map<Review, String> reviewsConUsuarios = new HashMap<>();

            for (Review r : reviews) {
                String userName = (r.getUserLinked() != null) ? r.getUserLinked().getName() : "ANONYMOUS";
                reviewsConUsuarios.put(r, userName);
            }
            model.addAttribute("serie", serie);
            model.addAttribute("reviewsConUsuarios", reviewsConUsuarios);
            elementService.fullSet64Image();
            return "W-SingleElement"; 
        } else {
            return "redirect:/error";
        }
    }

}
