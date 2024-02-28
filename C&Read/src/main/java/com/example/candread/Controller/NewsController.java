package com.example.candread.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.candread.model.New;
import com.example.candread.repositories.NewRepository;

@Configuration
@Controller
@RequestMapping("/news")
public class NewsController {

     @Autowired
    private NewRepository newRepository;

    @GetMapping("/{id}")
    public ResponseEntity<New> getNewById(@PathVariable Long id) {
        Optional<New> newOptional = newRepository.findById(id);

        return newOptional.map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public String addNew(@ModelAttribute New newO, Model model) {

        try {
            New newPrueba = new New(newO.getTitle(), newO.getDescription(), newO.getDate(), newO.getLink());
            newRepository.save(newPrueba);
            model.addAttribute("successMessage", "¡Noticia guardada correctamente!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la noticia.");
        }
        return "redirect:/Admin";
    }
}
