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
  //imageFile: Blob;
}
