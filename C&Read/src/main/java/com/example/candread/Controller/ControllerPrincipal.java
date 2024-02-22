package com.example.candread.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.candread.model.User;

import jakarta.servlet.http.HttpSession;


@Controller
public class ControllerPrincipal {

    //Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, HttpSession session) {
        String username = getUserName(session);
        model.addAttribute("username", username);
    return "W-Main";
    }

    //Moverse a las bibliotecas
    @GetMapping("/Library")
    public String moveToLibrary(Model model, HttpSession session) {
        String username = getUserName(session);
        model.addAttribute("username", username);
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
