import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { Review } from '../models/review.model';
import { User } from '../models/user.model';

const BASE_URL_REVIEWS = '/api/reviews/';

@Injectable({ providedIn: 'root' })
export class ReviewsService {

	constructor(private httpClient: HttpClient) { }


	addOrUpdateReview(review: Review) {
		if (!review.id) {
			return this.addReview(review);
		} else {
			return this.updateReview(review);
		}
	}

	getReviewUserById(id:number): Observable<User>{
        return this.httpClient.get<User>(BASE_URL_REVIEWS + id + "/user")
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    console.error('Error:', error);
                    return throwError(() => new Error('Server error: ' + error.statusText));
                })
            );
    }

	private addReview(review: Review) {
        const reviewJson: any = {
			body: review.body,
            rating: review.rating,
            userId: review.userLinked?.id,
			elementId: review.element_id?.id
		};
		return this.httpClient.post(BASE_URL_REVIEWS, reviewJson).pipe(
			tap((data: any) => {
				const id = data.id;
				return { id } as any;
			}),
			catchError(error => this.handleError(error))
		);
	}

	private updateReview(review: Review) {
        const reviewJson: any = {
			body: review.body,
            rating: review.rating,
            userId: review.userLinked?.id
		};
		return this.httpClient.put<Review>(BASE_URL_REVIEWS + review.id, reviewJson).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}
}
