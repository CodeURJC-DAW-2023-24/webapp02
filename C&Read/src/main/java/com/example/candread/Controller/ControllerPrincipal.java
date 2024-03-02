package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingRepository;
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
    private ElementService elementService;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
     @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {

        elementService.fullSet64Image();

        //CAROUSEL IMG
        List<Element> elementosEstreno = elementRepository.findTop4ByOrderByIdDesc();
        for (int i = 0; i < elementosEstreno.size(); i++) {
            Element e = elementosEstreno.get(i);
            String img = e.getBase64Image();
            String finalimg = "data:image/jpg;base64,"+ img;
            model.addAttribute("firstSlide"+i, finalimg);
        }
        
        // Pasamos los datos a la vista
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
        int numberBooks = 0;
        int numberFilms = 0;
        int numberSeries = 0;
        String typeNow;
        int limit = 0;

        elementService.fullSet64Image();

        //I extract the actual User from the model to use his Id
        User user = (User) model.getAttribute("user");

        //FOR AND SWITCH CASE TO GET ALL NUMBER OF MEDIA REGISTERED IN THE USER
        limit = user.getElements().size();
        for(int i = 0; i<limit; i = i+1){
            typeNow = user.getElements().get(i).getType();
            switch (typeNow) {
                case "LIBRO":
                    numberBooks = numberBooks +1;
                    break;
                case "SERIE":
                    numberSeries = numberSeries +1;
                    break;
                case "PELICULA":
                    numberFilms = numberFilms + 1;
                default:
                    break;
            }
        }
        Long userid =user.getId();
        //Page<Review> books= reviewRepository.findByUserLinked(userid, pageable);
        Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);
        model.addAttribute("PersonalBooks", userBooks);
        model.addAttribute("PersonalBookshasPrev", userBooks.hasPrevious());
		model.addAttribute("PersonalBookshasNext", userBooks.hasNext());
		model.addAttribute("PersonalBooksnextPage", userBooks.getNumber()+1);
		model.addAttribute("PersonalBooksprevPage", userBooks.getNumber()-1);

        Page<Element> userSeries = pagingRepository.findByUsersIdAndType(userid, "SERIE", pageable);
        model.addAttribute("PersonalSeries", userSeries);
        model.addAttribute("PersonalSerieshasPrev", userSeries.hasPrevious());
		model.addAttribute("PersonalSerieshasNext", userSeries.hasNext());
		model.addAttribute("PersonalSeriesnextPage", userSeries.getNumber()+1);
		model.addAttribute("PersonalSeriesprevPage", userSeries.getNumber()-1);

        Page<Element> userFilms = pagingRepository.findByUsersIdAndType(userid, "PELICULA", pageable);
        model.addAttribute("PersonalFilms", userFilms);
        model.addAttribute("PersonalFilmshasPrev", userFilms.hasPrevious());
		model.addAttribute("PersonalFilmshasNext", userFilms.hasNext());
		model.addAttribute("PersonalFilmsnextPage", userFilms.getNumber()+1);
		model.addAttribute("PersonalFilmsprevPage", userFilms.getNumber()-1);

        model.addAttribute("numBooks", numberBooks);
        model.addAttribute("numSeries", numberSeries);
        model.addAttribute("numFilms", numberFilms);
        
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
