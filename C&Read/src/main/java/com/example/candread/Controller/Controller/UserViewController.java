package com.example.candread.Controller.Controller;
//permite al usuario ver su version personalizada de las vistas y cambiar la ruta para poner el nombre del usuario

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{username}")
public class UserViewController {
    
    @GetMapping("/Main")
    public String main(@PathVariable String username, Model model) {
        // Puedes agregar atributos al modelo si es necesario, y también puedes acceder a la variable de ruta {username}
        model.addAttribute("username", username);
        
        return "W-Main";  
    }
}