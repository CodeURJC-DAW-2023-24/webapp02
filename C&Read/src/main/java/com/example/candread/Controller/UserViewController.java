package com.example.candread.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.candread.Security.RepositoryUserDetailsService;
import com.example.candread.controller.ControllerPrincipal;
import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.services.ElementService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{username}")
public class UserViewController {
    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private ElementService elementService;

    @GetMapping("/Main")
    public String main(@PathVariable String username, Model model, HttpServletRequest request)
            throws SQLException, IOException {

        elementService.fullSet64Image();

        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);

        return "W-Main";
    }

}
