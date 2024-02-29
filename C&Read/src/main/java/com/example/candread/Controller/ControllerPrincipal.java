package com.example.candread.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ControllerPrincipal {

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private PagingRepository pagingRepository;

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

        model.addAttribute("blobi", base64Image);
        model.addAttribute("blobi", imagesList);
        

        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);
        return "W-Main";
    }

    // Move to Library
    @GetMapping("/Library")
    public String moveToLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

        int pageNumber = page.orElse(0);
        int pageSize = 6;
        pageable = PageRequest.of(pageNumber, pageSize);

        Optional<Element> elementOptional = elementRepository.findById((long) 1);
        Element element = elementOptional.orElseThrow();
        Blob blob = element.getImageFile();
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        // System.out.println(base64Image);

        inputStream.close();

        model.addAttribute("blobi", base64Image);

        Page<Element> books= pagingRepository.findByType("LIBRO", pageable);
        model.addAttribute("books", books);
        model.addAttribute("hasPrev", books.hasPrevious());
		model.addAttribute("hasNext", books.hasNext());
		model.addAttribute("nextPage", books.getNumber()+1);
		model.addAttribute("prevPage", books.getNumber()-1);
        
    return "W-Library"; 
    }

    // move to LogIn
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

    public void setElementsImage64(long id) throws SQLException, IOException {
         Optional<Element> elementOptional = elementRepository.findById(id);
        Element element = elementOptional.orElseThrow();
        Blob blob = element.getImageFile();
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        element.setBase64Image(base64Image);

       
        inputStream.close();
    }
}
