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
  base64Image: string;
  imageFile: Blob;
  generos: string[];
  reviews: Review[];
}
