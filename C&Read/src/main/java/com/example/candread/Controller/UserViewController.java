package com.example.candread.Controller;
//permite al usuario ver su version personalizada de las vistas y cambiar la ruta para poner el nombre del usuario

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.candread.Security.RepositoryUserDetailsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/{username}")
public class UserViewController {
    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @GetMapping("/Main")
    public String main(@PathVariable String username, Model model, HttpSession session) {
        // Puedes agregar atributos al modelo si es necesario, y también puedes acceder a la variable de ruta {username}
        model.addAttribute("username", username);
        //User user = (User) session.getAttribute("user");
        UserDetails a = (UserDetails) session.getAttribute(username);
        if (a != null) {
            model.addAttribute("user", a);
        }
        return "W-Main";  
    }
    
}
