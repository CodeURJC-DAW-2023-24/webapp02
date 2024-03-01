package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.ElementService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/Library")
public class LibraryController {

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    private ElementService elementService;


    @GetMapping("/")
    public String moveToLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();


        Page<Element> books= pagingRepository.findByType("LIBRO", pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
        model.addAttribute("hasNext", books.hasNext());
        model.addAttribute("nextPage", books.getNumber()+1);
        model.addAttribute("prevPage", books.getNumber()-1);

    return "W-Library"; 
    }

    @GetMapping("/Genre")
    public String genreFilter(Model model, HttpSession session, @RequestParam("genre") String genre, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        Page<Element> books= pagingRepository.findByGenres(genre, pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }

    @GetMapping("/Season")
    public String seasonFilter(Model model, HttpSession session, @RequestParam("season") String season, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findBySeason(season, pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }

    @GetMapping("/Country")
    public String countryFilter(Model model, HttpSession session, @RequestParam("country") String country, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByCountry(country, pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }

    @GetMapping("/State")
    public String stateFilter(Model model, HttpSession session, @RequestParam("state") String state, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByState(state, pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }

    /*@GetMapping("/Year")
    public String yearFilter(Model model, HttpSession session, @RequestParam("year") String year, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();
        
        Page<Element> books= pagingRepository.findByYear(year, pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }*/

}
