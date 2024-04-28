import { User } from "./user.model";
import { Element as ElementC } from "./element.model";

export interface Review {
	id?: number;
	body: string;
	rating: number;
	element_id?: ElementC;
	userLinked: User;
}
