import { Component, Input } from '@angular/core';
import { Element as ElementComponent} from '../models/element.model';
import { User } from '../models/user.model';
import { Review } from '../models/review.model';
import { UsersService } from '../services/user.service';
import { ReviewsService } from '../services/review.service';

@Component({
  selector: 'app-reviews',
  templateUrl: '../Htmls/W-Review.component.html',
  styleUrls: ['../Css/S-SingleElement.css', '../Css/S-Review.css']
})

export class ReviewsComponent {

  @Input() user: User | undefined;
  @Input() elementR!: ElementComponent;
  reviews: Review[] = [];
  usersReviews: Map<number, string> = new Map<number, string>;
  review: Review | undefined;
  reviewRating: number = 0;
  reviewBody: string = '';

  constructor(private userService: UsersService, private reviewService: ReviewsService) { }

  ngOnInit() {
    this.getElementReviews();
  }
  //get element reviews:
  getElementReviews() {
    if (this.elementR && this.elementR.reviews) {
      this.reviews = this.elementR.reviews;
    }
  }

  getUsersOfReview() {
    for (let review of this.reviews) {
      if (review.user_id !== undefined) {
        let u: User = review.user_id;
        if (review.id !== undefined) {
          this.usersReviews.set(review.id, u.name);
        }
      }
    }
  }

  setModalReviewValues(review?: Review) {
    this.review = review;
    if (review?.rating != undefined) {
      this.reviewRating = review.rating;
    } else {
      this.reviewRating = 0;
    }
    if (review?.body != undefined) {
      this.reviewBody = review.body;
    } else {
      this.reviewBody = '';
    }
  }

  guardarReview() {
    this.review = {
      body: this.reviewBody,
      rating: this.reviewRating,
      user_id: this.user,
      element_id: this.elementR
    };
    this.reviewService.addOrUpdateReview(this.review).subscribe({
      next: () => {
        this.reviews.push(this.review!);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }
}