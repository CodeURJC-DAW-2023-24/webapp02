package com.example.candread.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.candread.model.Element;
import com.example.candread.model.New;
import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.NewRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.ElementService;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class ControllerPrincipal {

    @Autowired
    private NewRepository newRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private PagingRepository pagingRepository;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {
        userService.fullSet64Image();
        elementService.fullSet64Image();

        // CAROUSEL IMG
        List<Element> elementosEstreno = elementRepository.findTop4ByOrderByIdDesc();
        for (int i = 0; i < elementosEstreno.size(); i++) {
            Element e = elementosEstreno.get(i);
            String img = e.getBase64Image();
            String finalimg = "data:image/jpg;base64," + img;
            model.addAttribute("firstSlide" + i, finalimg);
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

    @GetMapping("/downloadNames")
    @ResponseBody
    public ResponseEntity<String> downloadNames(Model model, HttpSession session, Pageable pageable) {
        List<String> names = obtenerNombresDeLibros(model, pageable); 

        // Convierte la lista de nombres a una cadena separada por saltos de línea
         String content = String.join("\n", names);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "nombres_de_libros.txt");

    return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    

    private List<String> obtenerNombresDeLibros(Model model, Pageable pageable) {
        // Lógica para obtener los nombres de los libros
        
        User user = (User) model.getAttribute("user");
        Long userid =user.getId();
        //Page<Review> books= reviewRepository.findByUserLinked(userid, pageable);
        Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);

        List<String> names = new ArrayList<>();

        for (Element book : userBooks.getContent()) {
            names.add(book.getName()); // Reemplaza "getNombre()" con el método real para obtener el nombre del libro
        }
        
        // Añade más nombres según tu lógica
        return names;
    }


    // moverse a la pantalla de administrador
    @GetMapping("/Admin")
    public String moveToAdmin(Model model) throws SQLException, IOException {
        userService.fullSet64Image();;
        return "W-Admin";
    }

    @PostMapping(value = { "/error", "/loginerror" })
    public String moveToErrorOrLoginError(Model model) {
        return "W-Error";
    }

    @GetMapping(value = { "/error", "/loginerror" })
    public String moveToErrorLoginError(Model model) {
        return "W-Error";
    }

    

}
