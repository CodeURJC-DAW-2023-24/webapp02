package com.example.candread.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.ReviewRepository;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ElementRepository elementRepository;

    @PostMapping("/add")
    public String addReview(@RequestParam("userRating") int userRating, @RequestParam("userReview") String userReview,
            @RequestParam("elementId") Long elementId, Model model, HttpServletRequest request) {

        try {
            Review newReview = new Review(userReview, userRating);
            if(elementId!=null){
                Optional<Element> optionalElement = elementRepository.findById(elementId);
                Element element = (Element) optionalElement.get();
                newReview.setElementLinked(element);
                //User user = (User) model.getAttribute("user");
                //newReview.setUserLinked(user);
    
                reviewRepository.save(newReview);
    
                model.addAttribute("successMessage", "¡Review guardado correctamente!");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la review.");
        }

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "redirect:/SingleElement/" + elementId;
    }
}
