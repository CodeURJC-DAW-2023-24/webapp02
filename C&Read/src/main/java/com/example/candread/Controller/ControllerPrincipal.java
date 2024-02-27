package com.example.candread.Controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.candread.model.New;
import com.example.candread.model.User;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class ControllerPrincipal {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private UserService userService;

    //Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) {

        String u = null;
        if (request.getUserPrincipal() != null) {
            String name = request.getUserPrincipal().getName();
            User user = userRepository.findByName(name).orElseThrow();
            u=user.getName();
        }
        model.addAttribute("username", u);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        /* 
        userService.insertUsers();
        UserDetails username = (UserDetails) session.getAttribute("user");
        if (username!=null) {
            model.addAttribute("username", username.getUsername());
        }
        model.addAttribute("username", username);

        List<New> news = newRepository.findAll();
        Collections.reverse(news);
        List<New> newNews = news.subList(0, Math.min(news.size(), 3));
        model.addAttribute("news", newNews);*/
    return "W-Main";
    }

    //Moverse a las bibliotecas
    @GetMapping("/Library")
    public String moveToLibrary(HttpSession session, Model model, HttpServletRequest request) {

        String name = request.getUserPrincipal().getName();
        User user = userRepository.findByName(name).orElseThrow();
        model.addAttribute("username", user.getName());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
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
    public String moveToIniSes(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken()); 
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
