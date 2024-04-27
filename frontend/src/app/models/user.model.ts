import { Element } from "../models/element.model";

export interface User {
	id?: number;
	name: string;
  roles: string[];
  elements?: Element[];
  password: string;
  listasDeElementos: Map<string, number[]>;
}
