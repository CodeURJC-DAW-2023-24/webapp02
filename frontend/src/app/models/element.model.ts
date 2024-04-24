import { Review } from "./review.model";
import { User } from "./user.model";

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
  generos: string[];
  users: User[];
  usersFavourited: User[];
  reviews: Review[];
  imageFile: Blob;
}
