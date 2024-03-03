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
import com.example.candread.services.UserService;

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

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String getSingleElement(@PathVariable("id") Long id, Model model) throws SQLException, IOException {
        userService.fullSet64Image();
        if(id!=null){
            Optional<Element> optionalElement = elementRepository.findById(id);
            if (optionalElement.isPresent()) {
                Element serie = optionalElement.get();
                List<Review> reviews = serie.getReviews();
                Map<Review, String> reviewsConUsuarios = new HashMap<>();
                int totalRating=0;
                for (Review r : reviews) {
                    String userName = (r.getUserLinked() != null) ? r.getUserLinked().getName() : "ANONYMOUS";
                    reviewsConUsuarios.put(r, userName);
                    totalRating+=r.getRating();
                }
                double averageRating = 0;
                if(totalRating!=0){
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
        }else{
            return "redirect:/error";
        }
    }

}
