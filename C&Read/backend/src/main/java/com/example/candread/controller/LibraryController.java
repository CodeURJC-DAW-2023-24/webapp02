package com.example.candread.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.example.candread.services.ElementService;
import com.example.candread.services.PagingService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/Library")
public class LibraryController {

    @Autowired
    private PagingService pagingService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementRepository elementRepository;

    private List<Element> recomendation(User mainUser, List<Element> elements) {
        
        Map<Integer, List<Element>> filmRate = new HashMap<>();
        Map<String, Integer> genreAppear = new HashMap<>(); 

        // Estract User Favorite List
        List<Element> userFavorites = new ArrayList<>();
        List<Long> userFavoritesId  = mainUser.getListasDeElementos().get("Favoritos");
        for (Long id: userFavoritesId){
            Optional<Element> element = elementRepository.findById(id);
            Element e = element.get();
            userFavorites.add(e);
        }
        // Estract User Reviews
        List<Review> userReviews = mainUser.getReviews();
        
        // Save rate and films, films which are in favorite have a rate 6.
        filmRate.put(6, userFavorites);
        for(Review review: userReviews){
            int rate = review.getRating();
            Element element = review.getElementLinked();
            if (filmRate.containsKey(rate)){
                filmRate.get(rate).add(element);
            }else{
                List<Element> newList = new ArrayList<>();
                newList.add(element);
                filmRate.put(rate, newList);
            }
        }

        // Go through filmRate and take the rate of each genre.
        for(Map.Entry<Integer, List<Element>> entry : filmRate.entrySet()){
            int rate = entry.getKey();
            List<Element> elementsList = entry.getValue();

            for (Element e: elementsList){
                List<String> elementGenres = e.getGeneros();
                for (String genre: elementGenres){
                    if (genreAppear.containsKey(genre)){
                        int genreRate = genreAppear.get(genre);
                        genreRate += rate;
                        genreAppear.put(genre, genreRate);
                    }else{
                        genreAppear.put(genre, rate);
                    }
                }
            }

        }

        // Sort element:
        elements.sort((element1, element2) -> {
            int score1 = calculateScore(element1, genreAppear);
            int score2 = calculateScore(element2, genreAppear);
            return Integer.compare(score2, score1);
        });


        return elements;
    }

    private Integer calculateScore(Element e1, Map<String, Integer> genresByScore) {

        int score = 0;
        List<String> elementGenres = e1.getGeneros();
        for (String genre : elementGenres) {
            if (genresByScore.containsKey(genre)) {
                score += genresByScore.get(genre);
            }
        }
        return score;
    }

    private Page<Element> getPaging (String type, Pageable pageable, User mainUser){
        List<Element> e = elementService.repoFindAll();

        if (mainUser != null) {
            List<Element> recomendation = recomendation(mainUser, e);
            
            //Page<Element> booksPage = pagingRepository.findByTypeAndRecommendations("LIBRO", recomendation, pageable);
            Page<Element> booksPage = pagingService.repoFindByTypeAndRecommendations(type, recomendation, pageable);

            List<Element> allBooks = booksPage.getContent(); // Obtener todos los elementos de la página
            List<Element> booksInRecommendationOrder = recomendation.stream()
                .filter(allBooks::contains) // Mantener solo los elementos presentes en la lista de recomendaciones
                .collect(Collectors.toList());

            return new PageImpl<>(booksInRecommendationOrder, booksPage.getPageable(), booksPage.getTotalElements());
        } else {
            //return pagingRepository.findByType(type, pageable);
            return pagingService.repoFindByType(type, pageable);
        }
    }

    @GetMapping("/Books")
    public String moveToBookLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable) throws SQLException, IOException {

        userService.fullSet64Image();
        elementService.fullSet64Image();
        
        // Page attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);
        
        User mainUser = (User) model.getAttribute("user");
        Page<Element> books = getPaging("LIBRO", pageable, mainUser);

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
        elementService.fullSet64Image();

        // Page Attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        User mainUser = (User) model.getAttribute("user");
        Page<Element> books = getPaging("LIBRO", pageable, mainUser);

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
        elementService.fullSet64Image();

        //Page Attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        User mainUser = (User) model.getAttribute("user");
        Page<Element> films = getPaging("PELICULA", pageable, mainUser);

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
        elementService.fullSet64Image();

        //Page Attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        User mainUser = (User) model.getAttribute("user");
        Page<Element> films = getPaging("PELICULA", pageable, mainUser);

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
        elementService.fullSet64Image();

        // Page Attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        User mainUser = (User) model.getAttribute("user");
        Page<Element> series = getPaging("SERIE", pageable, mainUser);

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
        elementService.fullSet64Image();

        // Page Attributes
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageSize = (pageNumber + 1) * pageSize;
        pageable = PageRequest.of(0, pageSize);

        User mainUser = (User) model.getAttribute("user");
        Page<Element> series = getPaging("SERIE", pageable, mainUser);

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

        //Page<Element> books = pagingRepository.findByTypeAndGenres("LIBRO", genre, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndGenres("LIBRO", genre, pageable);
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

        //Page<Element> films = pagingRepository.findByTypeAndGenres("PELICULA", genre, pageable);
        Page<Element> films = pagingService.repoFindByTypeAndGenres("PELICULA", genre, pageable);
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

        //Page<Element> series = pagingRepository.findByTypeAndGenres("SERIE", genre, pageable);
        Page<Element> series = pagingService.repoFindByTypeAndGenres("SERIE", genre, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndSeason("LIBRO", season, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndSeason("LIBRO", season, pageable);
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

        //Page<Element> series = pagingRepository.findByTypeAndSeason("PELICULA", season, pageable);
        Page<Element> films = pagingService.repoFindByTypeAndSeason("PELICULA", season, pageable);
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

    @GetMapping("/Series/Season/js")
    public String SerieseasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        //Page<Element> films = pagingRepository.findByTypeAndSeason("SERIE", season, pageable);
        Page<Element> series = pagingService.repoFindByTypeAndSeason("SERIE", season, pageable);
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

    @GetMapping("/Books/Country/js")
    public String BooksCountryFilterjs(Model model, HttpSession session, @RequestParam("country") String country,
            @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request)
            throws SQLException, IOException {
        userService.fullSet64Image();
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        //Page<Element> books = pagingRepository.findByTypeAndCountry("LIBRO", country, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndCountry("LIBRO", country, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndCountry("PELICULA", country, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndCountry("PELICULA", country, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndCountry("SERIE", country, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndCountry("SERIE", country, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndState("LIBRO", state, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndState("LIBRO", state, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndState("PELICULA", state, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndState("PELICULA", state, pageable);
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

        //Page<Element> books = pagingRepository.findByTypeAndState("SERIE", state, pageable);
        Page<Element> books = pagingService.repoFindByTypeAndState("SERIE", state, pageable);
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
