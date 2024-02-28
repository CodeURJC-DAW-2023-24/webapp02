package com.example.candread.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.candread.Security.RepositoryUserDetailsService;
import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{username}")
public class UserViewController {
    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/Main")
    public String main(@PathVariable String username, Model model, HttpServletRequest request) {

        String name = request.getUserPrincipal().getName();
        User user = userRepository.findByName(name).orElseThrow();
        model.addAttribute("username", user.getName());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        return "W-Main";  
    }

}
