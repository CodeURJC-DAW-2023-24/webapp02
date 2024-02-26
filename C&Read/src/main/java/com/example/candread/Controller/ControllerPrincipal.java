package com.example.candread.Controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.News;
import com.example.candread.model.User;
import com.example.candread.repositories.NewRepository;

import jakarta.servlet.http.HttpSession;


@Controller
public class ControllerPrincipal {


    @Autowired
    private NewRepository newRepository;

    //Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, @RequestParam(required = false) String usuario) {
        if (usuario==null) {
            usuario = null;
        }
        List<News> news = newRepository.findAll();
        Collections.reverse(news);
        List<News> newNews = news.subList(0, Math.min(news.size(), 3));
        model.addAttribute("username", usuario);
        model.addAttribute("news", newNews);
    return "W-Main";
    }

    //Moverse a las bibliotecas
    @GetMapping("/Library")
    public String moveToLibrary(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    return "W-Library";
    }

    //moverse a un elemento de la biblioteca
    @GetMapping("/SingleElement")
    public String moveToSingleScreen(Model model, HttpSession session) {
        String username = getUserName(session);
        model.addAttribute("username", username);
    return "W-SingleElement";
    }

    //moverse a iniciar sesión
    @GetMapping("/LogIn")
    public String moveToIniSes(Model model) {
    //  model.addAttribute("ses", "sesión");
    return "W-LogIn";
    }

    //moverse a registrarse
    @GetMapping("/SignIn")
    public String moveToReg(Model model) {
        //  model.addAttribute("ses", "sesión");
    return "W-SignIn";
    }

    //moverse al perfil
    @GetMapping("/Profile")
    public String moveToPerfil(Model model, HttpSession session) {
        String username = getUserName(session);
        model.addAttribute("username", username);
    return "W-Profile";
    }

    //moverse a la pantalla de administrador
    @GetMapping("/Admin")
    public String moveToAdmin(Model model, HttpSession session) {
        String username = getUserName(session);
        model.addAttribute("username", username);
    return "W-Admin";
    }

    private String getUserName(HttpSession session){
        String username = null;
        User user = (User) session.getAttribute("user");
        if (user!=null) {
            username = user.getName();
        }
        return username;
    }
}
