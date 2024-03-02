package com.example.candread.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.New;
import com.example.candread.services.NewService;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@Controller
@RequestMapping("/news")
public class NewsController {

     @Autowired
    private NewService newService;

    @PostMapping("/add")
    public String addNew(@RequestParam("title") String title, @RequestParam("date") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date, @RequestParam("description") String description, @RequestParam("link") String link, Model model, HttpServletRequest request) {

        try {
            New newPrueba = new New(title, description, date, link);
            newService.saveNew(newPrueba);
            model.addAttribute("successMessage", "¡Noticia guardada correctamente!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la noticia.");
        }

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("token", request.getAttribute("_csrf").toString());
        }

        return "redirect:/Admin";
    }
}
