import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { Element as ElementComponent } from '../models/element.model';
import { User } from '../models/user.model';
import { Review } from '../models/review.model';
import { UsersService } from '../services/user.service';
import { ReviewsService } from '../services/review.service';
import { forkJoin } from 'rxjs';

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
  }

  //get element reviews:
  getElementReviews() {
    var listReviews: any[] = [];
    if (this.elementR && this.elementR.reviews) {
      listReviews = this.elementR.reviews;
      const observables = listReviews.map(review => {
        return this.reviewService.getReviewUserById(review.id!);
      });

      forkJoin(observables).subscribe({
        next: (users: User[]) => {
          // Asignar los usuarios a las reviews correspondientes
          for (let i = 0; i < listReviews.length; i++) {
            listReviews[i].userLinked = users[i];
          }
          this.reviews = listReviews;
          this.setRating();
          this.getUsersOfReview();
        },
        error: (error) => {
          console.error('Error:', error);
        }
      });
    }
  }

  getUsersOfReview() {
    for (let review of this.reviews) {
      if (review.userLinked !== undefined) {
        let u: User = review.userLinked;
        if (review.id !== undefined) {
          this.usersReviews.set(review.id, u);
          this.setReviewsImage(u, review.id);
        }
      }
    }
  }

  setReviewsImage(u: User, r: number){
    if(u && u.id !== undefined){
      this.userService.getUserImage(u.id).subscribe((imageData) => {
        if(imageData){
          const blob = new Blob([imageData], {type: 'image/jpeg'});
          this.usersReviewsImages.set(r, URL.createObjectURL(blob));
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
    let totalRating: number = 0;
    const numberOfReviews = this.reviews.length;
    for(let rev of this.reviews){
      const rat: number = Number(rev.rating);
      totalRating = totalRating + rat;
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
      userLinked: this.user!,
      element_id: this.elementR
    };
    this.reviewService.addOrUpdateReview(this.review).subscribe({
      next: (response: any) => {
        this.review!.id = response.id;
        if (this.review) {
          const existingReviewIndex = this.reviews.findIndex(r => r.id === response.id);
          if (existingReviewIndex !== -1) {
            this.reviews[existingReviewIndex] = this.review;
            this.setRating();
          } else {
            this.reviews.push(this.review);
            this.setReviewsImage(this.review.userLinked!, response.id);
            this.setRating();
          }
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    })
  }
}
