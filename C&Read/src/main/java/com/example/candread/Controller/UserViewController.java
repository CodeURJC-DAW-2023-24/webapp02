package com.example.candread.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.security.RepositoryUserDetailsService;
import com.example.candread.services.ElementService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/Main")
    public String main(@PathVariable String username, Model model, HttpServletRequest request)
            throws SQLException, IOException {

        elementService.fullSet64Image();
        userService.fullSet64Image();

        // CAROUSEL IMG
        List<Element> elementosEstreno = elementRepository.findTop4ByOrderByIdDesc();
        for (int i = 0; i < elementosEstreno.size(); i++) {
            Element e = elementosEstreno.get(i);
            String img = e.getBase64Image();
            String finalimg = "data:image/jpg;base64," + img;
            model.addAttribute("firstSlide" + i, finalimg);
        }

        List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        model.addAttribute("news", newsList);

        return "W-Main";
    }

    // moverse al perfil
    @GetMapping("/Profile")
    public String moveToPerfil(Model model, HttpSession session, @RequestParam("page") Optional<Integer> page,
            Pageable pageable) throws SQLException, IOException {

        // LOAD ELEMENTS LISTS
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);
        int numberBooks = 0;
        int numberFilms = 0;
        int numberSeries = 0;

        // NUMBER OF GENRES
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

        List<String> genresNow = new ArrayList<>();
        int limit = 0;

        elementService.fullSet64Image();

        User user = (User) model.getAttribute("user");

        // FOR AND SWITCH CASE TO GET ALL NUMBER OF MEDIA REGISTERED IN THE USER
        if (user != null) {
            String typeNow;
            limit = user.getElements().size();
            for (int i = 0; i < limit; i = i + 1) {
                typeNow = user.getElements().get(i).getType();
                switch (typeNow) {
                    case "LIBRO":
                        numberBooks = numberBooks + 1;
                        break;
                    case "SERIE":
                        numberSeries = numberSeries + 1;
                        break;
                    case "PELICULA":
                        numberFilms = numberFilms + 1;
                        break;

                    default:
                        break;
                }
            }
            String genreNow;
            for (int i = 0; i < limit; i = i + 1) {
                genresNow = user.getElements().get(i).getGeneros();
                int limitNow = genresNow.size();
                for (int j = 0; j < limitNow; j = j + 1) {
                    genreNow = genresNow.get(j);
                    switch (genreNow) {
                        case "ACCION":
                            numberAction = numberAction + 1;
                            break;
                        case "TERROR":
                            numberTerror = numberTerror + 1;
                            break;
                        case "AVENTURA":
                            numberAventura = numberAventura + 1;
                            break;
                        case "MISTERIO":
                            numberMisterio = numberMisterio + 1;
                            break;
                        case "ROMANCE":
                            numberRomance = numberRomance + 1;
                            break;
                        case "CIENCIAFICCION":
                            numberCiencia = numberCiencia + 1;
                            break;
                        case "DRAMA":
                            numberDrama = numberDrama + 1;
                            break;
                        case "INFANTIL":
                            numberInfantil = numberInfantil + 1;
                            break;
                        case "COMEDIA":
                            numberComedia = numberComedia + 1;
                            break;
                        case "FANTASIA":
                            numberFantasia = numberFantasia + 1;
                            break;
                        case "SOBRENATURAL":
                            numberSobrenatural = numberSobrenatural + 1;
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
            Long userid = user.getId();
            Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);
            model.addAttribute("PersonalBooks", userBooks);
            model.addAttribute("PersonalBookshasPrev", userBooks.hasPrevious());
            model.addAttribute("PersonalBookshasNext", userBooks.hasNext());
            model.addAttribute("PersonalBooksnextPage", userBooks.getNumber() + 1);
            model.addAttribute("PersonalBooksprevPage", userBooks.getNumber() - 1);

            Page<Element> userSeries = pagingRepository.findByUsersIdAndType(userid, "SERIE", pageable);
            model.addAttribute("PersonalSeries", userSeries);
            model.addAttribute("PersonalSerieshasPrev", userSeries.hasPrevious());
            model.addAttribute("PersonalSerieshasNext", userSeries.hasNext());
            model.addAttribute("PersonalSeriesnextPage", userSeries.getNumber() + 1);
            model.addAttribute("PersonalSeriesprevPage", userSeries.getNumber() - 1);

            Page<Element> userFilms = pagingRepository.findByUsersIdAndType(userid, "PELICULA", pageable);
            model.addAttribute("PersonalFilms", userFilms);
            model.addAttribute("PersonalFilmshasPrev", userFilms.hasPrevious());
            model.addAttribute("PersonalFilmshasNext", userFilms.hasNext());
            model.addAttribute("PersonalFilmsnextPage", userFilms.getNumber() + 1);
            model.addAttribute("PersonalFilmsprevPage", userFilms.getNumber() - 1);

            // ATTRIBUTES FOR FAVOURITES
            Page<Element> userBFavourites = pagingRepository.findByUsersFavouritedIdAndType(userid, "LIBRO", pageable);
            model.addAttribute("PersonalBFavs", userBFavourites);
            model.addAttribute("PersonalBFavshasPrev", userBFavourites.hasPrevious());
            model.addAttribute("PersonalBFavshasNext", userBFavourites.hasNext());
            model.addAttribute("PersonalBFavsnextPage", userBFavourites.getNumber() + 1);
            model.addAttribute("PersonalBFavsprevPage", userBFavourites.getNumber() - 1);

            Page<Element> userSFavourites = pagingRepository.findByUsersFavouritedIdAndType(userid, "SERIE", pageable);
            model.addAttribute("PersonalSFavs", userSFavourites);
            model.addAttribute("PersonalSFavshasPrev", userSFavourites.hasPrevious());
            model.addAttribute("PersonalSFavshasNext", userSFavourites.hasNext());
            model.addAttribute("PersonalSFavsnextPage", userSFavourites.getNumber() + 1);
            model.addAttribute("PersonalSFavsprevPage", userSFavourites.getNumber() - 1);

            Page<Element> userFFavourites = pagingRepository.findByUsersFavouritedIdAndType(userid, "PELICULA",
                    pageable);
            model.addAttribute("PersonalFFavs", userFFavourites);
            model.addAttribute("PersonalFFavshasPrev", userFFavourites.hasPrevious());
            model.addAttribute("PersonalFFavshasNext", userFFavourites.hasNext());
            model.addAttribute("PersonalFFavsnextPage", userFFavourites.getNumber() + 1);
            model.addAttribute("PersonalFFavsprevPage", userFFavourites.getNumber() - 1);

            // ATTRIBUTES FOR CHART 1 - TYPE OF ELEMENT
            model.addAttribute("numBooks", numberBooks);
            model.addAttribute("numSeries", numberSeries);
            model.addAttribute("numFilms", numberFilms);

            // ATTRIBUTES FOR CHART 2 - GENRE OF ELEMENT
            model.addAttribute("numAccion", numberAction);
            model.addAttribute("numTerror", numberTerror);
            model.addAttribute("numAventura", numberAventura);
            model.addAttribute("numMisterio", numberMisterio);
            model.addAttribute("numRomance", numberRomance);
            model.addAttribute("numCiencia", numberCiencia);
            model.addAttribute("numDrama", numberDrama);
            model.addAttribute("numInfantil", numberInfantil);
            model.addAttribute("numComedia", numberComedia);
            model.addAttribute("numFantasia", numberFantasia);
            model.addAttribute("numSobrenatural", numberSobrenatural);
            model.addAttribute("numNovela", numberNovela);
            model.addAttribute("numJuvenil", numberJuvenil);
        }
        // LOAD PROFILE ELEMENTS
        userService.fullSet64Image();
        return "W-Profile";

    }

}
