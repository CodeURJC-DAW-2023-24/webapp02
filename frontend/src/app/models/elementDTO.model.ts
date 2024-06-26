import { Review } from "./review.model";

export interface Element {
	id?: number;
	name: string;
	description: string;
  year: number;
  type: string;
  season: string;
  state: string;
  author: string;
  country: string;
  imageFile: Blob;
  genres: string[];
  reviews: Review[];
}
