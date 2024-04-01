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
![image](https://github.com/CodeURJC-DAW-2023-24/webapp02/assets/80918271/6f12781e-c0da-4e07-a44b-5d898f9d8f8f)

## Instrucciones
Componentes Necesarios:
 - Para la Base de Datos:
   - Descargar MySQL Installer Community: https://dev.mysql.com/downloads/file/?id=526408
   - Versión de MySQL: 8.0.36  
 - Versión de java: Preferiblemente la versiñon de jdk más actualizada.
 - Versión de SpringBoot: 3.2.2 <br> 

Pasos a seguir para su correcta ejecución:
 - Clonar el repositorio de GitHub:
   Para ello hay dos metodos:
    - Utiliza el comando git clone seguido de la URL del repositorio de GitHub que deseas clonar. Puedes obtener la URL en la página principal del repositorio en GitHub. Por ejemplo:
      - git clone https://github.com/usuario/nombre-repositorio.git (Asegúrate de reemplazar "https://github.com/usuario/nombre-repositorio.git" con la URL real del repositorio que deseas clonar)
      - Abre el proyecto en Visual Studio Code: Una vez que el repositorio se ha clonado, puedes abrir el proyecto en Visual Studio Code utilizando el comando: code nombre-repositorio.
    - Otro metodo si se utiliza Visual Studio Code sería:
      - Asegurarse de tener la extendión de github correspondiente que sería: x
      - Ir a Source Controll clickeando en el siguiente icono: o con el atajo de teclado: Cntrl + Shift + G.
      - Una vez dentro, puklsar en Clone Repository:
      - En la pestaña emergente insertar la url del repositorio y darle a enter:
      - Elegir en que carpeta se querra guardar el projecto clonado y una vez guardado, simplemente aceptar el abrirlo.
      - Listo, ya estará el projecto disponible en su entorno de desarrollom, podemos pasar al siguiente paso.
 - Abrir el WorkBench:
   - Darle a Añadir una nueva conexión y aladirla en el puerto 3306 con Hostname localhost
   - Crear una base de datos llamada bdd_candread
   - Crear otro usuario de la base de datos con estos datos:
     - Username=rudy
     - Password=Rudy2442.

Siguiendo estos pasos ya puede empezar a usar la aplicación, simplemente vaya al siguiente icono: y pulse el boton de ejecutar:
Para ver la aplicación en el navegador introduzca la siguiente ruta: https://localhost:8443 y acepte los riesgos de la conexión.
   

## Diagramas Entidades
![image](https://github.com/CodeURJC-DAW-2023-24/webapp02/assets/80918271/808a9b05-a7d7-4c9f-a374-0c2832f2e62a)

## Diagramas Clases Y Templates
![image](https://github.com/CodeURJC-DAW-2023-24/webapp02/assets/80918271/f8bd9a04-b503-4f2a-b663-7b110b3a5131)

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
   - Descripción: Configuración de la seguridad junto a David|funcionalidad de insercion de un elemento en la base de datos|insercion de datos de ejemplo junto con Juan|modificacion de element para que pueda tener la cadena codificada en base 64 pero no se vea reflejado en las bases de datos|configuracion correcta en el properties para la conexion de batos de datos|mustache empleado en single element para que single element muestre la imagen del elemento
   - 5 Commits:
     - 1: (https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/2161ca54869c1cbdc34c2fedbd4f58f9361417fb)
     - 2: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/9c9e521a046389af6ae908b8d13288b687073688
     - 3: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/ac2173e4168c8d378b4491ebaf4bc64fd58b228d
     - 4: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/c27738f986a7a78eae0807883f734b697f7952b7
     - 5: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/60f46ab25f2a666e7dc74aa6f9b82dd2e1e5c4d2
   - 5 Ficheros:
     - 1: Security Configuration: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/JaviFase2/C%26Read/src/main/java/com/example/candread/Security/SecurityConfiguration.java
     - 2: UserDetailService.java: [https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/JaviFase2/C%26Read/src/main/java/com/example/candread/Security/RepositoryUserDetailsService.java]
     - 3: ElementController: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/JaviFase2/C%26Read/src/main/java/com/example/candread/Controller/ElementController.java
     - 4: ElementService.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/JaviFase2/C%26Read/src/main/java/com/example/candread/services/ElementService.java
     - 5: W-Admin.html: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/JaviFase2/C%26Read/src/main/resources/templates/W-Admin.html
- Juan Álvarez Loeches:
   - Descripción: Definición del Elemento, que recoge todos los tipos de media recogidos en la aplicación | Guardar y trabajar con datos de ejemplo en la base de datos | Adaptar el formato de las imágenes a uno que acepte la BBDD | Gráficos de las estadísticas del usuario en relación a sus elementos guardados y favoritos | Definir las relaciones entre los Elementos y Usuarios, los guardados y los favoritos, y con las reviews | Mostrar en el perfil de usuario sus elementos guardados y sus favoritos por separado | Funciones para permitir al usuario guardar o agregar a favoritos un elemento, evitando que agregue uno que ya posee | 
   - 5 Commits:
     - 1: Function for Users to Add elements by themselves: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/f0a4be50f212e8405d97f8478ff11095cd4683d9
     - 2: Added Function to save Elements & Save Favourites: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/5b061929bdfca21acfc75370990b7f4ee3bbc05e
     - 3: More Examples for the Users Charts: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/6f2037072df768a41ce58a21cfd537ecb6ce6ddb
     - 4: Creacion Elemento de Ejemplo y su servicio: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/8cdcdaac8f62dd415a9fc77459c6da6233eb9961
     - 5: Imagen Local en Base de Datos con Blob: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/4694b9b2433d98ea1ad9dd10c0b8ccaf95b4c401
   - 5 Ficheros:
     - 1: ElementService.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/Juan_Fase2/C%26Read/src/main/java/com/example/candread/services/ElementService.java
     - 2: Element.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/Juan_Fase2/C%26Read/src/main/java/com/example/candread/model/Element.java
     - 3: ElementController.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/Juan_Fase2/C%26Read/src/main/java/com/example/candread/Controller/ElementController.java
     - 4: UserViewController.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/Juan_Fase2/C%26Read/src/main/java/com/example/candread/Controller/UserViewController.java  (La función moveToPerfil se encontraba anteriormente en el ControllerPrincipal.java pero fue trasladada, así que se sustituye por la clase a la que fue trasladada.)
     - 5:LibraryController.java: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/Juan_Fase2/C%26Read/src/main/java/com/example/candread/Controller/LibraryController.java 
 - Aurora María Fernández Basanta:
    - Descripción: Organización de los archivos | Añadido Header y Cards con Mustache | Registro e Inicio de sesion en el Header intercalado con Perfil dependiendo del estado del usuario | Pasarle el usuario iniciado al model de serie | Permitirle al usuario    editar su nombre, su imagen de perfil y su imagen de banner | Añadir la página al https y configurar correctamente la seguridad despues de su creación por parted de David y Javier | Añadir el CSRF a los formularios | Permitir añadir noticias y mostrar las 3 más recientes | Crear base de datos de review | Permitir ver las review de cada elemento a todo tipo de usuarios | Permitirle añadir reseña a los usuarios registrados y los admins y seguidamente ver su reseña | Añadir informacion sobre cada elemento a la vista SingleElement | Implementación del carusel con los ultimos elementos añadidos al sistema | Refactorizar el codigo para dejarlo limpio
   - 5 Commits:
     - 1: Fixed Segurity: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/a0f8a5162e6669767bf169ea390aefce72425815 
     - 2: News add to Database: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/19d4ea78ec6a8db5ae2d5b183b24a1405219d028 
     - 3: Reviews Configuration Completed: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/f0d1865cfe22bd52bb846496186cd80337fd6de2 
     - 4: Carousel Configuration: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/8135f6a785f6169b2430557d8b0b03d17fd2cecb 
     - 5: User Profile Edit: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/55cf6238f3397bf24123d712ba7b59a197df4e25 
   - 5 Ficheros:
     - 1: SecurityConfiguration: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/AuroraFase2/C%26Read/src/main/java/com/example/candread/Security/SecurityConfiguration.java
     - 2: ReviewController: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/AuroraFase2/C%26Read/src/main/java/com/example/candread/Controller/ReviewController.java
     - 3: DefaultModelAttribute: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/AuroraFase2/C%26Read/src/main/java/com/example/candread/Controller/DefaultModelAttributes.java
     - 4: Review: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/AuroraFase2/C%26Read/src/main/java/com/example/candread/model/Review.java
     - 5: UserController: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/AuroraFase2/C%26Read/src/main/java/com/example/candread/Controller/UserController.java
- Javier Vallejo Fernández:
   - Descripción: Parametros opcionales enañadir elemento|Funcionalidad de editar elemento|Creacion de la API REST de series|Finalizacion de api rest de usuarios para la creacion y edicion de usuarios| principio de utilizacion de tokens jwt para permitir el uso de admin desde postman |creacion de llamada auth a la api tambien para llamdadas api desde postman| modificacion de añadir elemento para permitir que el genero sea input texto| DTO de use| elementAPi
   - 5 Commits:
     - 1: Series Api: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/72518c1ebb45d2c64f67e8283f3d12c138109574
     - 2: Editar elemento: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/fad9a630abc7cfff17dafca3c18f1bc7ee66b782
     - 3: Mejorado funcionalidad de añadir elemento: https://github.com/CodeURJCDAW202324/webapp02/commit/1118cfe6797e7fbc3265da991a91d7c3836581d3
     - 4: Añadir imagen despues de crear usuario de api: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/32c0dcb8c74d0b0b08f1ea66f170d3a9ce5e7d45
     - 5: Ficheros JWT para llamda postman: https://github.com/CodeURJC-DAW-2023-24/webapp02/commit/71cf2b833ea017103fbcf40df6e2be011519ef46
   - 5 Ficheros:
     - 1: Series Api: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/c56e9507cd3577e78d5b7fad0523cc1ab54fe46e/backend/src/main/java/com/example/candread/apicontroller/SeriesApiController.java
     - 2: ElementController: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/c56e9507cd3577e78d5b7fad0523cc1ab54fe46e/backend/src/main/java/com/example/candread/controller/ElementController.java
     - 3: USER DTO: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/c56e9507cd3577e78d5b7fad0523cc1ab54fe46e/backend/src/main/java/com/example/candread/dto/UserDTO.java
     - 4: Login Controller: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/c56e9507cd3577e78d5b7fad0523cc1ab54fe46e/backend/src/main/java/com/example/candread/apicontroller/auth/LoginController.java
     - 5: Token Provider: https://github.com/CodeURJC-DAW-2023-24/webapp02/blob/c56e9507cd3577e78d5b7fad0523cc1ab54fe46e/backend/src/main/java/com/example/candread/security/jwt/JwtTokenProvider.java
