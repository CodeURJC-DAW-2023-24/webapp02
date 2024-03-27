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
import com.example.candread.services.ElementService;
import com.example.candread.services.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ElementService elementService;

    @PostMapping("/add")
    public String addReview(@RequestParam("userRating") int userRating, @RequestParam("userReview") String userReview,
            @RequestParam("elementId") Long elementId, Model model, HttpServletRequest request) {

        try {
            Review newReview = new Review(userReview, userRating);
            if(elementId!=null){
                //Optional<Element> optionalElement = elementRepository.findById(elementId);
                Optional<Element> optionalElement = elementService.repoFindById(elementId);
                Element element = (Element) optionalElement.get();
                newReview.setElementLinked(element);
                User user = (User) model.getAttribute("user");
                newReview.setUserLinked(user);
    
                //reviewRepository.save(newReview);
                reviewService.repoSaveReview(newReview);
    
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

    @PostMapping("/edit/add")
    public String editReview (
    @RequestParam("reviewID") Long reviewId,
    @RequestParam("userRating") int userRating, 
    @RequestParam("userReview") String userReviewText,
    @RequestParam("elementId") Long elementId, 
    Model model, HttpServletRequest request){
        
        //Optional<Review> reviewToEdit = reviewRepository.findById(reviewId);
        Optional<Review> reviewToEdit = reviewService.repoFindById(reviewId);
        Review reviewEditted = reviewToEdit.orElseThrow();

        reviewEditted.setRating(userRating);
        reviewEditted.setBody(userReviewText);

        //Optional<Element> optionalElement = elementRepository.findById(elementId);
        //Element element = (Element) optionalElement.get();

        //reviewRepository.save(reviewEditted);
        reviewService.repoSaveReview(reviewEditted);
        //reviewRepository.delete(reviewEditted);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
        return "redirect:/SingleElement/" + elementId;
    }

    @PostMapping("/delete")
    public String deleteReview (
    @RequestParam("reviewID") Long reviewId,
    @RequestParam("elementId") Long elementId, 
    Model model, HttpServletRequest request){
        
        //Optional<Review> reviewToEdit = reviewRepository.findById(reviewId);
        Optional<Review> reviewToEdit = reviewService.repoFindById(reviewId);
        Review reviewEditted = reviewToEdit.orElseThrow();

        //reviewEditted.setRating(0);
        //reviewEditted.setBody(null);

        //Optional<Element> optionalElement = elementRepository.findById(elementId);
        //Element element = (Element) optionalElement.get();

        //reviewRepository.save(reviewEditted);
        //reviewRepository.delete(reviewEditted);
        reviewService.repoDeleteReview(reviewEditted);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
        return "redirect:/SingleElement/" + elementId;
    }
}
