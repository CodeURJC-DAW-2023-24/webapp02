package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.ElementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ControllerPrincipal {

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    private ElementService elementService;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {

        elementService.fullSet64Image();

        // CAROUSEL IMG
        List<Element> elementosEstreno = elementRepository.findTop4ByOrderByIdDesc();
        for (int i = 0; i < elementosEstreno.size(); i++) {
            Element e = elementosEstreno.get(i);
            String img = e.getBase64Image();
            String finalimg = "data:image/jpg;base64," + img;
            model.addAttribute("firstSlide" + i, finalimg);
        }

        // Pasamos los datos a la vista
        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);
        return "W-Main";
    }

    // move to LogIn
    @GetMapping("/LogIn")
    public String moveToIniSes(Model model, HttpServletRequest request) {
        return "W-LogIn";
    }

    // moverse a registrarse
    @GetMapping("/SignIn")
    public String moveToReg(Model model, HttpServletRequest request) {
        return "W-SignIn";
    }

    // moverse a la pantalla de administrador
    @GetMapping("/Admin")
    public String moveToAdmin(Model model) {
        return "W-Admin";
    }

    @PostMapping(value = { "/error", "/loginerror" })
    public String moveToErrorOrLoginError(Model model) {
        return "W-Error";
    }

    @GetMapping(value = { "/error", "/loginerror" })
    public String moveToErrorLoginError(Model model) {
        return "W-Error";
    }

}
