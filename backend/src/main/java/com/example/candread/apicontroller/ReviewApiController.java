package com.example.candread.apicontroller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.dto.ReviewDTO;
import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.User;
import com.example.candread.services.ElementService;
import com.example.candread.services.ReviewService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/reviews")
public class ReviewApiController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReview(@PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO,
            HttpServletRequest request) throws URISyntaxException {

        Optional<Review> optReview = reviewService.repoFindById(id);

        if (optReview.isPresent()) {
            Review review = (Review) optReview.get();
            if (reviewDTO.getBody() != null) {
                review.setBody(reviewDTO.getBody());
            }
            if (reviewDTO.getRating() != 0) {
                review.setRating(reviewDTO.getRating());
            }
            reviewService.repoSaveReview(review);
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> uploadReview(@RequestBody ReviewDTO reviewDTO,
            HttpServletRequest request) throws URISyntaxException {

        Review review = new Review(reviewDTO.getBody(), reviewDTO.getRating());
        Optional<User> userOptional = userService.repoFindById(reviewDTO.getUserId());
        User user = userOptional.get();
        Optional<Element> elementOptional = elementService.repoFindById(reviewDTO.getElementId());
        Element element = elementOptional.get();
        review.setUserLinked(user);
        review.setElementLinked(element);
        reviewService.repoSaveReview(review);
        Long reviewId = review.getId();
        String reviewUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(reviewId)
                .toUriString();

        return ResponseEntity.created(new URI(reviewUrl)).build();
    }

}
