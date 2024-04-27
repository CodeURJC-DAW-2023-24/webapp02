import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Review } from '../models/review.model';

const BASE_URL = '/api/reviews/';

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

	private addReview(review: Review) {
        const reviewJson: any = {
			body: review.body,
            rating: review.rating,
            userId: review.userLinked?.id,
			elementId: review.element_id?.id
		};
		return this.httpClient.post(BASE_URL, reviewJson).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private updateReview(review: Review) {
        const reviewJson: any = {
			body: review.body,
            rating: review.rating,
            userId: review.userLinked?.id
		};
		return this.httpClient.put<Review>(BASE_URL + review.id, reviewJson).pipe(
			catchError(error => this.handleError(error))
		);
	}

	private handleError(error: any) {
		console.error(error);
		return throwError(() => new Error("Server error (" + error.status + "): " + error.text()));
	}
}
