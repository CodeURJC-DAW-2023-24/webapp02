package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ControllerPrincipal {
 @GetMapping("/")
 public String greeting(Model model) {
//  model.addAttribute("name", "World");
 return "Reg";
 }
}
