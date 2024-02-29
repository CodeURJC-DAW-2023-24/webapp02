package com.example.candread.Controller;

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
import org.springframework.web.bind.annotation.PostMapping;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import jakarta.servlet.http.HttpServletRequest;
@Controller
public class ControllerPrincipal {

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private ElementRepository elementRepository;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
     @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {

        // Adición de un objeto element de ejemplo a la base de datos.
        // elementService.insertElement();

        Optional<Element> elementOptional = elementRepository.findById((long) 1);
        Element element = elementOptional.orElseThrow();
        Blob blob = element.getImageFile();
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        // System.out.println(base64Image);

        inputStream.close();

        model.addAttribute("blobi", base64Image);;

        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);
    return "W-Main";
    }

    // Moverse a las bibliotecas
    @GetMapping("/Library")
    public String moveToLibrary(Model model) {
        return "W-Library";
    }

    // moverse a iniciar sesión
    @GetMapping("/LogIn")
    public String moveToIniSes(Model model, HttpServletRequest request) {
        return "W-LogIn";
    }

    // moverse a registrarse
    @GetMapping("/SignIn")
    public String moveToReg(Model model, HttpServletRequest request) {
    return "W-SignIn";
    }

    // moverse al perfil
    @GetMapping("/Profile")
    public String moveToPerfil(Model model) {
    return "W-Profile";
    }

    // moverse a la pantalla de administrador
    @GetMapping("/Admin")
    public String moveToAdmin(Model model) {
    return "W-Admin";
    }

    @PostMapping(value = {"/error", "/loginerror"})
    public String moveToErrorOrLoginError(Model model) {
    return "W-Error";
    }

    @GetMapping(value = {"/error", "/loginerror"})
    public String moveToErrorLoginError(Model model) {
    return "W-Error";
    }
}
