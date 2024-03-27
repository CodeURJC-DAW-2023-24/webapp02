package com.example.candread.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.ElementService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/Library")
public class LibraryController {

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    private ElementService elementService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElementRepository elementRepository;

    private List<Element> recomendador(User mainUser, List<Element> elements) {
        List<User> users = userRepository.findAll();

        Map<Element, Integer> reviewsPorElemento = new HashMap<>();
        Map<Element, Set<String>> generosPorElemento = new HashMap<>();

        // por cada usuario, que calificación tiene un elemento.
        for (User user : users) {
            for (Review review : user.getReviews()) {
                if (review.getElementLinked().getType().equals("LIBRO")){
                    int calificacion = review.getRating();
                    int ca = reviewsPorElemento.getOrDefault(review.getElementLinked(), 0);
                    ca += calificacion;
                    reviewsPorElemento.put(review.getElementLinked(), ca);
                }
            }
        }
        // generos que tiene cada elemento
        for (Element element : elements) {
            Set<String> generos = new HashSet<>(element.getGeneros());
            generosPorElemento.put(element, generos);
        }

        return elementRecomendation(reviewsPorElemento, generosPorElemento, mainUser, elements);
    }

    private List<Element> elementRecomendation(Map<Element, Integer> reviewsPorElemento,
            Map<Element, Set<String>> generosPorElemento, User mainUser, List<Element> elements) {

        // sacar favoritos y reviews del usuario
        List<Review> reviewsMainUser = mainUser.getReviews();

        // lista de que puntuación tiene el usuario por cada genero
        Map<String, Integer> elementosRevisados = new HashMap<>();

        // Inicializar listas de elementos por género: G1 -> E1, E2..ETC
        Map<String, List<Element>> elementosPorGenero = new HashMap<>();
        for (Element element : elements) {
            for (String genero : element.getGeneros()) {
                elementosPorGenero.putIfAbsent(genero, new ArrayList<>());
                elementosPorGenero.get(genero).add(element);
            }
        }

        // Por cada genero, que puntuacion le ha dado el usuario
        // Calcular puntajes por género
        for (Map.Entry<String, List<Element>> entry : elementosPorGenero.entrySet()) {
            String genero = entry.getKey();
            List<Element> elementosDelGenero = entry.getValue();
            
            int c = 0;
            for (Element elemento : elementosDelGenero) {
                int cantidadEstrellas = 0; // Reiniciar la cantidad de estrellas para cada elemento
                
                //si el usuario tiene una review de algun elemento de este genero, este genero aumenta su puntuación.
                for (Review review : reviewsMainUser) {
                    if (review.getElementLinked().getName().equals(elemento.getName())) { // Verificar si la review está asociada al elemento actual
                        cantidadEstrellas += review.getRating(); // Sumar la calificación de la revisión al total
                    }
                }
                elementosRevisados.put(genero, c += cantidadEstrellas);
                // Guardar la cantidad total de estrellas que el usuario ha dado para este género
            }

            // Ordenar el mapa de mayor a menor
            List<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(elementosRevisados.entrySet());
            listaOrdenada.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            // Reconstruir el mapa ordenado
            Map<String, Integer> elementosRevisadosOrdenados = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> e : listaOrdenada) {
                elementosRevisadosOrdenados.put(e.getKey(), e.getValue());
            }

            elementosRevisados = elementosRevisadosOrdenados;

            // ordenamos los elementos del genero para que esten los mejor valorados delante:
            elementosDelGenero.sort((elemento1, elemento2) -> {
                // Obtener la puntuación de cada elemento
                // reviews por elemento guarad la suma de las reviews otorgadas al elemento,
                // sacamos de ahi la comparación
                int puntuacionElemento1 = reviewsPorElemento.getOrDefault(elemento1, 0);
                int puntuacionElemento2 = reviewsPorElemento.getOrDefault(elemento2, 0);

                // Comparar las puntuaciones en orden descendente
                return Integer.compare(puntuacionElemento2, puntuacionElemento1);
            });

            elementosPorGenero.put(genero, elementosDelGenero);
            // al salir de aqui tenemos:
            // - elementosRevisados -> Puntuación que tiene el usuario por cada genero de manera ordenada, tipo, usuario1 tiene de aventura un 10 por qu etiene os reseñas de un elemento con este genero
            // - elementosPorGenero -> Elementos que tiene cada genero ordenados por puntuación total de otros usuarios
        }

        List<Element> elementosOrdenados = new ArrayList<>();
        Set<Element> elementosAgregados = new HashSet<>(); // Conjunto para rastrear elementos ya agregados

        //por cada genero que el usuario prefiera, más consuma:
        for (Map.Entry<String, Integer> entry : elementosRevisados.entrySet()) {
            String genero = entry.getKey();
            List<Element> elOfGenre = elementosPorGenero.get(genero);

            //se agregan lso elementos por orden de mejor valorados en elementos ordenados, el cual será el resultado:
            for (Element elemento : elOfGenre) {
                if (!elementosAgregados.contains(elemento)) { // Verificar si el elemento ya está en la lista
                    elementosOrdenados.add(elemento);
                    elementosAgregados.add(elemento); // Agregar el elemento al conjunto de elementos agregados
                }
            }
        }

        return elementosOrdenados;
    }

    @GetMapping("/Books")
    public String moveToBookLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable) throws SQLException, IOException {

        User mainUser = (User) model.getAttribute("user");
        List<Element> b = elementRepository.findByType("LIBRO"); 
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books;
        if (mainUser != null) {
            List<Element> recomendation = recomendador(mainUser, b);
            
            Page<Element> booksPage = pagingRepository.findByTypeAndRecommendations("LIBRO", recomendation, pageable);

            List<Element> allBooks = booksPage.getContent(); // Obtener todos los elementos de la página
            List<Element> booksInRecommendationOrder = recomendation.stream()
                .filter(allBooks::contains) // Mantener solo los elementos presentes en la lista de recomendaciones
                .collect(Collectors.toList());

            books = new PageImpl<>(booksInRecommendationOrder, booksPage.getPageable(), booksPage.getTotalElements());
        } else {
            books = pagingRepository.findByType("LIBRO", pageable);
        }
        model.addAttribute("elements", books);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        return "W-Library";
    }

    @GetMapping("/Books/js")
    public String moveToBookLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;

        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByType("LIBRO", pageable);

        model.addAttribute("elements", books);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("hasPrev", pageSize > 10);
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Films")
    public String moveToFilmLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable) throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> films = pagingRepository.findByType("PELICULA", pageable);
        model.addAttribute("elements", films);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("hasPrev", films.hasPrevious());
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber() + 1);
        model.addAttribute("prevPage", films.getNumber() - 1);

        return "W-Library";
    }

    @GetMapping("/Films/js")
    public String moveToFilmLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;

        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        elementService.fullSet64Image();

        Page<Element> films = pagingRepository.findByType("PELICULA", pageable);
        model.addAttribute("elements", films);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("hasPrev", pageSize > 10);
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber() + 1);
        model.addAttribute("prevPage", films.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Series")
    public String moveToSeriesLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable) throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> series = pagingRepository.findByType("SERIE", pageable);
        model.addAttribute("elements", series);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("hasPrev", series.hasPrevious());
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber() + 1);
        model.addAttribute("prevPage", series.getNumber() - 1);

        return "W-Library";
    }

    @GetMapping("/Series/js")
    public String moveToSeriesLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;

        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        elementService.fullSet64Image();

        Page<Element> series = pagingRepository.findByType("SERIE", pageable);
        model.addAttribute("elements", series);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("hasPrev", pageSize > 10);
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber() + 1);
        model.addAttribute("prevPage", series.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Books/Genre/js")
    public String BookGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndGenres("LIBRO", genre, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Films/Genre/js")
    public String FilmGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> films = pagingRepository.findByTypeAndGenres("PELICULA", genre, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", films);
        model.addAttribute("hasPrev", films.hasPrevious());
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber() + 1);
        model.addAttribute("prevPage", films.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Series/Genre/js")
    public String SeriesGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> series = pagingRepository.findByTypeAndGenres("SERIE", genre, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", series);
        model.addAttribute("hasPrev", series.hasPrevious());
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber() + 1);
        model.addAttribute("prevPage", series.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Books/Season/js")
    public String BookSeasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndSeason("LIBRO", season, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Films/Season/js")
    public String FilmSeasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> series = pagingRepository.findByTypeAndSeason("PELICULA", season, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", series);
        model.addAttribute("hasPrev", series.hasPrevious());
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber() + 1);
        model.addAttribute("prevPage", series.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Series/Season/js")
    public String SerieseasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> films = pagingRepository.findByTypeAndSeason("SERIE", season, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", films);
        model.addAttribute("hasPrev", films.hasPrevious());
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber() + 1);
        model.addAttribute("prevPage", films.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Books/Country/js")
    public String BooksCountryFilterjs(Model model, HttpSession session, @RequestParam("country") String country,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndCountry("LIBRO", country, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Films/Country/js")
    public String FilmsCountryFilterjs(Model model, HttpSession session, @RequestParam("country") String country,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndCountry("PELICULA", country, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Series/Country/js")
    public String countryFilterjs(Model model, HttpSession session, @RequestParam("country") String country,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndCountry("SERIE", country, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Books/State/js")
    public String BookStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndState("LIBRO", state, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Films/State/js")
    public String FilmStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndState("PELICULA", state, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }

    @GetMapping("/Series/State/js")
    public String SerieStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books = pagingRepository.findByTypeAndState("SERIE", state, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber() + 1);
        model.addAttribute("prevPage", books.getNumber() - 1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "W-LibraryFragment";
    }
}
