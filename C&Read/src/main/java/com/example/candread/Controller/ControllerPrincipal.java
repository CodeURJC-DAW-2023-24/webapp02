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
import com.example.candread.repositories.PagingRepository;
import com.example.candread.repositories.ReviewRepository;
import com.example.candread.services.ElementService;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import jakarta.persistence.criteria.CriteriaBuilder.Case;
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
    private PagingRepository pagingProfileRepository;

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

    // Move to Library
    // @GetMapping("/Library")
    // public String moveToLibrary(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page, Pageable pageable) throws SQLException, IOException {

    //     int pageNumber = page.orElse(0);
    //     int pageSize = 6;
    //     pageable = PageRequest.of(pageNumber, pageSize);

    //     elementService.fullSet64Image();


    //     Page<Element> books= pagingRepository.findByType("LIBRO", pageable);
    //     model.addAttribute("books", books);
    //     model.addAttribute("hasPrev", books.hasPrevious());
	// 	model.addAttribute("hasNext", books.hasNext());
	// 	model.addAttribute("nextPage", books.getNumber()+1);
	// 	model.addAttribute("prevPage", books.getNumber()-1);
        
    // return "W-Library"; 
    // }

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
        //int nB, nF, nS = 0 NO LOS PONE TODOS A 0, PREGUNTAR POR QUÉ
        int numberBooks = 0;
        int numberFilms = 0;
        int numberSeries = 0;

        //NUMBER OF GENRES
        int numberAction = 0;
        int numberTerror = 0;
        int numberAventura = 0;
        int numberMisterio = 0;
        int numberRomance = 0;
        int numberCiencia = 0;
        int numberDrama = 0;
        int numberInfantil = 0;
        int numberComedia = 0;
        int numberFantasia = 0;
        int numberSobrenatural = 0;
        int numberNovela = 0;
        int numberJuvenil = 0;

        String typeNow;
        List<String> genresNow = new ArrayList<>();
        int limit = 0;

        elementService.fullSet64Image();

        //I extract the actual User from the model to use his Id
        User user = (User) model.getAttribute("user");

        //FOR AND SWITCH CASE TO GET ALL NUMBER OF MEDIA REGISTERED IN THE USER
        limit = user.getElements().size();
        for(int i = 0; i<limit; i = i+1){
            typeNow = user.getElements().get(i).getType();
            switch (typeNow){
                case "LIBRO":
                    numberBooks = numberBooks +1;
                    break;
                case "SERIE":
                    numberSeries = numberSeries +1;
                    break;
                case "PELICULA":
                    numberFilms = numberFilms + 1;
                    break;

                default:
                    break;
            }
            
        }
        String genreNow;
        for(int i = 0; i<limit; i = i+1){
            genresNow = user.getElements().get(i).getGeneros();
            int limitNow = genresNow.size();
            for(int j = 0; j<limitNow; j = j+1){
                genreNow = genresNow.get(j);
                switch (genreNow) {
                    case "ACCION":
                        numberAction = numberAction +1;
                        break;
                    case "TERROR":
                        numberTerror = numberTerror +1;
                        break;
                    case "AVENTURA":
                        numberAventura = numberAventura + 1;
                        break;
                    case "MISTERIO":
                        numberMisterio = numberMisterio +1;
                        break;
                    case "ROMANCE":
                        numberRomance = numberRomance +1;
                        break;
                    case "CIENCIAFICCION":
                        numberCiencia = numberCiencia + 1;
                        break; 
                    case "DRAMA":
                        numberDrama = numberDrama +1;
                        break;
                    case "INFANTIL":
                        numberInfantil = numberInfantil +1;
                        break;
                    case "COMEDIA":
                        numberComedia = numberComedia + 1;
                        break; 
                    case "FANTASIA":
                        numberFantasia = numberFantasia +1;
                        break;
                    case "SOBRENATURAL":
                        numberSobrenatural = numberSobrenatural +1;
                        break;
                    case "NOVELA":
                        numberNovela = numberNovela + 1;
                        break; 
                    case "JUVENIL":
                        numberJuvenil = numberJuvenil + 1;
                        break; 
                    default:
                        break;
                }
                
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

        model.addAttribute("numAccion", numberAction );
        model.addAttribute("numTerror", numberTerror);
        model.addAttribute("numAventura", numberAventura);
        model.addAttribute("numMisterio", numberMisterio);
        model.addAttribute("numRomance", numberRomance);
        model.addAttribute("numCiencia", numberCiencia);
        model.addAttribute("numDrama", numberDrama);
        model.addAttribute("numInfantil",numberInfantil );
        model.addAttribute("numComedia", numberComedia);
        model.addAttribute("numFantasia", numberFantasia);
        model.addAttribute("numSobrenatural", numberSobrenatural);
        model.addAttribute("numNovela", numberNovela);
        model.addAttribute("numJuvenil", numberJuvenil);
        
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
