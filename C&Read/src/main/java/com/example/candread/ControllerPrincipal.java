package com.example.candread;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ControllerPrincipal {
    @GetMapping("/")
    public String moveToReg(Model model) {
    //  model.addAttribute("ses", "sesión");
    return "Reg";
    }

    @GetMapping("/IniSes")
    public String moveToIniSes(Model model) {
    //  model.addAttribute("ses", "sesión");
    return "IniSes";
    }
}
