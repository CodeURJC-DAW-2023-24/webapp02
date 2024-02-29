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
import org.springframework.web.bind.annotation.RequestMethod;
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
        int pageSize = 6;
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
    
}
