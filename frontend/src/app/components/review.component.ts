import { Component, Input } from '@angular/core';
import { Element } from '../models/element.model';
import { User } from '../models/user.model';
import { Review } from '../models/review.model';
import { UsersService } from '../services/user.service';

@Component({
  selector: 'app-reviews',
  templateUrl: '../Htmls/W-Review.component.html',
  styleUrls: ['../Css/S-SingleElement.css', '../Css/S-Review.css']
})

export class ReviewsComponent {

  @Input() user: User | undefined;
  @Input() elementR: Element | undefined;
  reviews: Review[] = [];
  usersReviews: Map<number, string> = new Map<number, string>;
  review: Review | undefined;
  reviewRating: number = 0;
  reviewBody: string = '';

  constructor(private userService: UsersService) { }

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
        let userId: number = review.user_id;
        this.userService.getUser(userId).subscribe({
          next: (user: User) => {
            if (review.id !== undefined) {
              this.usersReviews.set(review.id, user.name);
            }
          }
        })
      }
    }
  }

  setModalReviewValues(review?: Review) {
    this.review = review;
    if (review?.rating != undefined) {
      this.reviewRating = review.rating;
    }else{
      this.reviewRating = 0;
    }
    if (review?.body != undefined) {
      this.reviewBody = review.body;
    }else{
      this.reviewBody = '';
    }
  }

  guardarReview() {
    console.log("EDITAR o GUARDAR RESEÑA " + this.reviewRating + " " + this.reviewBody);
  }
}