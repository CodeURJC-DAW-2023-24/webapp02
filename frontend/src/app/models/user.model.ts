export interface User {
	id?: number;
	name: string;
  roles: string[];
  elements: Element[];
  listasDeElementos: Map<string, number[]>;
}
