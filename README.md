# webapp02

## Nombre de la Aplicación
C&Read
## Integrantes
- Javier Vallejo Fernandez - j.vallejo.2019@alumnos.urjc.es - Javier-Vallejo
- David Rubio Caballero - d.rubio.2019@alumnos.urjc.es - David-Rubio-Caballlero
- Juan Álvarez Loeches - j.alvarezl.2019@alumnos.urjc.es - Juan-Alvarez-Loeches
- Aurora María Fernández Basanta - am.fernandez.2019@alumnos.urjc.es - Aurora-Fernandez
## Trello
Link al tablero de trello: https://trello.com/b/3sUtM4Yb
## Descripción de la APP
### - Entidades
* Usuario
* Contenido(Libros, series, películas)
* Noticia
* Reseña
### - Tipos y Permisos de los usuarios
* Usuario anónimo: Podrá visualizar el catálogo de contenido audiovisual de la web, pero no podrá registrar cuáles ha consumido ni dejar reseñas o ver estadísticas.
* Usuario registrado: Posee un perfil en el que puede registrar que contenidos audiovisuales ha consumido, escribir reseñas de estos y ver sus estadísticas personales.
* Usuario Administrador: Se encargará de mantener la web actualizada, ingresando y modificando los diferentes contenidos audiovisuales en ella, además de poder banear usuarios de la web.
### - Imágenes
* Utilizaremos imágenes para las fotos de las portadas de cada uno de los contenidos audiovisuales, y para que los usuarios posean foto de perfil como rasgo identificativo.
### - Gráficos
* Utilizaremos gráficos en los prefiles para mostrarle a los usuarios sus propias estadíasticas. Estos gráficos serán un gráfico circular o un gráfico de tabla. 
### - Tecnología complementaria
* Se utilizará tecnología de API REST para las bibliotecas de libros, series o películas.
* APIs a utilizar:
  * OpenLibrary para los libros (https://openlibrary.org/dev/docs/api/search)
  * AniList para anime y manga
  * Series y peliculas (https://apimovieseries-explication.vercel.app/)
### - Algoritmo o consulta avanzada
* Se implementará un algoritmo en la fase de búsqueda que modifique el contenido mostrado teniendo en cuenta las valoraciones de cada elemento. Además, habrá un algoritmo de recomendaciones personalizadas en base a la similitud de reseñas con otros usuarios (si otro usuario coincide en valoraciones altas con la mitad de nuestro contenido, se considerará que tiene gustos similares).
