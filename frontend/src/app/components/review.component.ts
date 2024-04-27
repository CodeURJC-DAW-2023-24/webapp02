import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Element as ElementComponent } from '../models/element.model';
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
  @Output() 
  rating = new EventEmitter<number>();

  reviews: Review[] = [];
  usersReviews: Map<number, User> = new Map<number, User>;
  usersReviewsImages: Map<number, string> = new Map<number, string>;
  review: Review | undefined;
  reviewRating: number = 0;
  reviewBody: string = '';

  constructor(private userService: UsersService, private reviewService: ReviewsService) { }

  ngOnInit() {
    this.getElementReviews();
    this.getUsersOfReview();
    this.setRating();
  }

  //get element reviews:
  getElementReviews() {
    if (this.elementR && this.elementR.reviews) {
      this.reviews = this.elementR.reviews;
    }
  }

  getUsersOfReview() {
    for (let review of this.reviews) {
      if (review.userLinked !== undefined) {
        let u: User = review.userLinked;
        if (review.id !== undefined) {
          this.usersReviews.set(review.id, u);
          this.setReviewsImage(u, review);
        }
      }
    }
  }

  setReviewsImage(u: User, r: Review){
    if(u && u.id !== undefined){
      this.userService.getUserImage(u.id).subscribe((imageData) => {
        if(imageData){
          const blob = new Blob([imageData], {type: 'image/jpeg'});
          this.usersReviewsImages.set(r.id!, URL.createObjectURL(blob));
        }
      });
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

  setRating(){
    let totalRating = 0;
    const numberOfReviews = this.reviews.length;
    for(let rev of this.reviews){
      totalRating += rev.rating;
    }

    if (numberOfReviews > 0) {
      const averageRating = totalRating / numberOfReviews;
      this.rating.emit(averageRating);
    }
  }
  guardarReview() {
    this.review = {
      id: this.review?.id,
      body: this.reviewBody,
      rating: this.reviewRating,
      userLinked: this.user,
      element_id: this.elementR
    };
    this.reviewService.addOrUpdateReview(this.review).subscribe({
      next: (response: any) => {
        if (this.review) {
          const existingReviewIndex = this.reviews.findIndex(r => r.id === this.review!.id);
          if (existingReviewIndex !== -1) {
            this.reviews[existingReviewIndex] = this.review;
          } else {
            this.reviews.push(this.review);
            this.setReviewsImage(this.review.userLinked!, response.id);
          }
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }
}