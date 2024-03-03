package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.ElementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/Library")
public class LibraryController {

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    private ElementService elementService;


    @GetMapping("/Books")
    public String moveToBookLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> books= pagingRepository.findByType("LIBRO", pageable);
        model.addAttribute("elements", books);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber()+1);
        model.addAttribute("prevPage", books.getNumber()-1);


    return "W-Library"; 
    }

    @GetMapping("/Books/js")
    public String moveToBookLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> books= pagingRepository.findByType("LIBRO", pageable);
        
        model.addAttribute("elements", books);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber()+1);
        model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }


    return "W-LibraryFragment"; 
    }

    @GetMapping("/Films")
    public String moveToFilmLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> films= pagingRepository.findByType("PELICULA", pageable);
        model.addAttribute("elements", films);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("hasPrev", films.hasPrevious());
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber()+1);
        model.addAttribute("prevPage", films.getNumber()-1);

    return "W-Library"; 
    }

    @GetMapping("/Films/js")
    public String moveToFilmLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> films= pagingRepository.findByType("PELICULA", pageable);
        model.addAttribute("elements", films);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("hasPrev", films.hasPrevious());
        model.addAttribute("hasNext", films.hasNext());
        model.addAttribute("nextPage", films.getNumber()+1);
        model.addAttribute("prevPage", films.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

    return "W-LibraryFragment"; 
    }

    @GetMapping("/Series")
    public String moveToSeriesLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> series= pagingRepository.findByType("SERIE", pageable);
        model.addAttribute("elements", series);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("hasPrev", series.hasPrevious());
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber()+1);
        model.addAttribute("prevPage", series.getNumber()-1);
        

    return "W-Library"; 
    }

    @GetMapping("/Series/js")
    public String moveToSeriesLibraryjs(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> series= pagingRepository.findByType("SERIE", pageable);
        model.addAttribute("elements", series);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("hasPrev", series.hasPrevious());
        model.addAttribute("hasNext", series.hasNext());
        model.addAttribute("nextPage", series.getNumber()+1);
        model.addAttribute("prevPage", series.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

    return "W-LibraryFragment"; 
    }
    
    
    @GetMapping("/Books/Genre/js")
    public String BookGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books= pagingRepository.findByTypeAndGenres("LIBRO", genre, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Films/Genre/js")
    public String FilmGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> films= pagingRepository.findByTypeAndGenres("PELICULA", genre, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", films);
        model.addAttribute("hasPrev", films.hasPrevious());
		model.addAttribute("hasNext", films.hasNext());
		model.addAttribute("nextPage", films.getNumber()+1);
		model.addAttribute("prevPage", films.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Series/Genre/js")
    public String SeriesGenreFilterjs(Model model, HttpSession session, @RequestParam("genre") String genre, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> series= pagingRepository.findByTypeAndGenres("SERIE", genre, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", series);
        model.addAttribute("hasPrev", series.hasPrevious());
		model.addAttribute("hasNext", series.hasNext());
		model.addAttribute("nextPage", series.getNumber()+1);
		model.addAttribute("prevPage", series.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Books/Season/js")
    public String BookSeasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndSeason("LIBRO",season, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Films/Season/js")
    public String FilmSeasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> series= pagingRepository.findByTypeAndSeason("PELICULA", season, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", series);
        model.addAttribute("hasPrev", series.hasPrevious());
		model.addAttribute("hasNext", series.hasNext());
		model.addAttribute("nextPage", series.getNumber()+1);
		model.addAttribute("prevPage", series.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }


    @GetMapping("/Series/Season/js")
    public String SerieseasonFilterjs(Model model, HttpSession session, @RequestParam("season") String season, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> films= pagingRepository.findByTypeAndSeason("SERIE", season, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", films);
        model.addAttribute("hasPrev", films.hasPrevious());
		model.addAttribute("hasNext", films.hasNext());
		model.addAttribute("nextPage", films.getNumber()+1);
		model.addAttribute("prevPage", films.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Books/Country/js")
    public String BooksCountryFilterjs(Model model, HttpSession session, @RequestParam("country") String country, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndCountry("LIBRO", country, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Films/Country/js")
    public String FilmsCountryFilterjs(Model model, HttpSession session, @RequestParam("country") String country, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndCountry("PELICULA", country, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }


    @GetMapping("/Series/Country/js")
    public String countryFilterjs(Model model, HttpSession session, @RequestParam("country") String country, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndCountry("SERIE", country, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }


    @GetMapping("/Books/State/js")
    public String BookStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndState("LIBRO", state, pageable);
        model.addAttribute("controllerRoute", "Books");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }


    @GetMapping("/Films/State/js")
    public String FilmStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndState("PELICULA", state, pageable);
        model.addAttribute("controllerRoute", "Films");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    @GetMapping("/Series/State/js")
    public String SerieStateFilterjs(Model model, HttpSession session, @RequestParam("state") String state, @RequestParam("page") Optional<Integer> page, Pageable pageable, HttpServletRequest request) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByTypeAndState("SERIE", state, pageable);
        model.addAttribute("controllerRoute", "Series");
        model.addAttribute("elements", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }
        
    return "W-LibraryFragment"; 
    }

    // @GetMapping("/Year")
    // public String yearFilter(Model model, HttpSession session, @RequestParam("year") String year, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
    //     int pageNumber = page.orElse(0);
    //     int pageSize = 10;
    //     pageable = PageRequest.of(pageNumber, pageSize);

    //     elementService.fullSet64Image();
        
    //     Page<Element> books= pagingRepository.findByYear(year, pageable);
    //     model.addAttribute("books", books);
    //     model.addAttribute("hasPrev", books.hasPrevious());
	// 	model.addAttribute("hasNext", books.hasNext());
	// 	model.addAttribute("nextPage", books.getNumber()+1);
	// 	model.addAttribute("prevPage", books.getNumber()-1);
        
    // return "W-Library"; 
    // }

}
