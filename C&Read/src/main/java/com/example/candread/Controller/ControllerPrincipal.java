package com.example.candread.Controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.candread.model.New;
import com.example.candread.model.User;
import com.example.candread.repositories.NewRepository;

import jakarta.servlet.http.HttpSession;


@Controller
public class ControllerPrincipal {


    @Autowired
    private NewRepository newRepository;

    //Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(HttpSession session, Model model) {

        //Adición de un objeto element de ejemplo a la base de datos.
        //elementService.insertElement();
        
        UserDetails username = (UserDetails) session.getAttribute("user");
        if (username!=null) {
            model.addAttribute("username", username.getUsername());
        }
        model.addAttribute("username", username);

        List<New> news = newRepository.findAll();
        Collections.reverse(news);
        List<New> newNews = news.subList(0, Math.min(news.size(), 3));
        model.addAttribute("news", newNews);
    return "W-Main";
    }

    //Moverse a las bibliotecas
    @GetMapping("/Library")
    public String moveToLibrary(HttpSession session, Model model) {
        UserDetails username = (UserDetails) session.getAttribute("user");
        model.addAttribute("username", username.getUsername());
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
        UserDetails username = (UserDetails) session.getAttribute("user");
        model.addAttribute("username", username.getUsername());
    return "W-Profile";
    }

    //moverse a la pantalla de administrador
    @GetMapping("/Admin")
    public String moveToAdmin(Model model, HttpSession session) {
        UserDetails username = (UserDetails) session.getAttribute("user");
        model.addAttribute("username", username.getUsername());
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
