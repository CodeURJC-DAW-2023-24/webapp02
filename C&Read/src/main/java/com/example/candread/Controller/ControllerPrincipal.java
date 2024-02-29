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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.model.Review;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingProfileRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.repositories.ReviewRepository;
import com.example.candread.services.ElementService;

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

    @Autowired
    private PagingProfileRepository pagingProfileRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ElementService elementService;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
     @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {

        elementService.fullSet64Image();

        

        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);
        return "W-Main";
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
    public String moveToPerfil(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);

        elementService.fullSet64Image();

        //I extract the actual User from the model to use his Id
        User user = (User) model.getAttribute("user");
        Long userid =user.getId();

        Page<Review> books= reviewRepository.findByUserLinked(userid, pageable);
        model.addAttribute("PersonalBooks", books);
        model.addAttribute("PersonalBookshasPrev", books.hasPrevious());
		model.addAttribute("PersonalBookshasNext", books.hasNext());
		model.addAttribute("PersonalBooksnextPage", books.getNumber()+1);
		model.addAttribute("PersonalBooksprevPage", books.getNumber()-1);
        
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
