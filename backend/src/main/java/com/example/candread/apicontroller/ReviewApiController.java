package com.example.candread.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.candread.model.Review;
import com.example.candread.services.ReviewService;


@RestController
@RequestMapping("api/reviews")
public class ReviewApiController {


    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public List<Review> getReviews() {
        return reviewService.repoFindAll();
    }
    

}
