# webapp02

# FASE 0:

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


# FASE 1:

## Pantallas
- Pantalla Principal:
  Esta sería la pantalla principal de la aplicación que tendría un registro de las películas/series/libros preferidos de la semana. Esta pantalla la pueden ver todos los usuarios.
  
- Pantalla Inicio de Sesión:
  Pantalla donde los usuarios con una cuenta pueden iniciar sesión.
  
- Pantalla Registro:
  Pantalla que permite a un usuario sin cuenta, crearse una cuenta en el sistema y pasar a ser un usuario registrado.
  
- Pantalla Pantalla Biblioteca:
  Pantalla que mostrara el catalogo de películas, libros o series dependiendo de lo que se desee ver. En este caso, para hacer de ejemplo se ha hecho la pantalla bliblioteca de películas.
  También servirá para mostrar las listas de los usuarios registrados en el sistema.
  
- Pantalla Elemento Biblioteca:
  Pantalla que mostrara toda la información de un elemento específico de la biblioteca. En el caso actual se ha puesto una seria como ejemplo.
  
- Pantalla Perfil:
  Pantalla que le mostrará a un usuario registrado su perfil con sus listas de peliculas/series/libros (o listas personalizadas) y sus estadísticas personales.
  
- Pantalla Administrador:
  Pantalla disponoble solo para los administradores que les permitirá añadir noticias a la plataforma o modificar/añadir algun elemento a la biblioteca.
  
- Editar Perfil PopUp:
  PopUp en la pantalla perfil que le permitirá al usuario editar su información de perfil.
  
- Editar Elemento Biblioteca PopUp:
  PopUp en la pantalla de adminitrador que le permitira a los usuarios administradores modíficar un elemento que ya esté en la biblioteca.

## Diagrama de Navegación

# FASE 2:

## Navegación

## Instrucciones

## Diagramas Entidades

## Diagramas Clases Y Templates

## Participación
 - David Ovidio Rubio Caballero:
   - Descripción:Funcionalidades de cada tipo de usuario y roles|Paginación de la biblioteca de Elements usando Mustache|Filtros en las biblioteca|Configuración de la seguridad junto a Javier| Agregar CSRF a los formularios| Gestión de registro e inicio de sesión de usuarios| Descarga de los titulos de los libros que tiene el usuario en propiedad, tecnologia extra|Nueva paginación para utilizar AJAX y hacer que la Library no se recargue por completo cada vez que aplico un filtro o busco más elementos|Página de error
   - 5 Commits: 
     - 1: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/2a59d1cc48dbaf77aa475d17b78970ff7d3d229c
     - 2: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/ef746f56f42583e9c4c78e1299bbd3e21409871d
     - 3: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/2ec3c3d40eba767f6613fd6a80f6a0d51c557980
     - 4: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/e15bc0f9941c4d5d6954567b8145b7efe1ce6289
     - 5: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/bcb3b763c2c3d5930c4e25bd5d5e863781a5b897
   - 5 Ficheros:
     - 1: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/DavidFase2/C%26Read/src/main/java/com/example/candread/Security/SecurityConfiguration.java
     - 2: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/DavidFase2/C%26Read/src/main/java/com/example/candread/repositories/PagingRepository.java
     - 3: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/DavidFase2/C%26Read/src/main/java/com/example/candread/Controller/LibraryController.java
     - 4: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/DavidFase2/C%26Read/src/main/java/com/example/candread/Controller/UserViewController.java
     - 5: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/DavidFase2/C%26Read/src/main/resources/static/Scripts/pages.js
 - Javier Vallejo Fernández:
   - Descripción:
   - 5 Commits:
     - 1
     - 2
     - 3
     - 4
     - 5 
   - 5 Ficheros:
     - 1
     - 2
     - 3
     - 4
     - 5 
 - Juan Álvarez Loeches:
   - Descripción:
   - 5 Commits:
     - 1
     - 2
     - 3
     - 4
     - 5 
   - 5 Ficheros:
     - 1
     - 2
     - 3
     - 4
     - 5 
 - Aurora María Fernández Basanta:
   - Descripción: Organización de los archivos | Añadido Header y Footer con Mustache | Registro e Inicio de sesion en el Header intercalado con Perfil dependiendo del estado del usuario | Pasarle el usuario iniciado a la sesión.
   - 5 Commits:
     - 1
     - 2
     - 3
     - 4
     - 5 
   - 5 Ficheros:
     - 1
     - 2
     - 3
     - 4
     - 5 
