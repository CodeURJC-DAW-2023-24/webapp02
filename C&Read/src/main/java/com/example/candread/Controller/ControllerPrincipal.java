package com.example.candread.controller;

import java.io.ByteArrayOutputStream;
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
<<<<<<< HEAD
import com.example.candread.services.UserService;
=======
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
>>>>>>> 4f5faffa14fad2d2002ff73e1ae1e9ffd06c67bf

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
        List<Element> elementsRelease = elementRepository.findTop4ByOrderByIdDesc();
        for (int i = 0; i < elementsRelease.size(); i++) {
            Element e = elementsRelease.get(i);
            String img = e.getBase64Image();
            String finalImg = "data:image/jpg;base64," + img;
            model.addAttribute("firstSlide" + i, finalImg);
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
    public ResponseEntity<byte[]> downloadNames(Model model, HttpSession session, Pageable pageable) throws IOException {
        List<String> names = obtenerNombresDeLibros(model, pageable); 

<<<<<<< HEAD
        // Convert the list of names to a string separated by line breaks
         String content = String.join("\n", names);
=======
        byte[] pdfBytes = generatePdf(names);

        // Convierte la lista de nombres a una cadena separada por saltos de línea
        String content = String.join("\n", names);
>>>>>>> 4f5faffa14fad2d2002ff73e1ae1e9ffd06c67bf

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "nombres_de_libros.pdf");

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private List<String> obtenerNombresDeLibros(Model model, Pageable pageable) {
        // Logic to get the names of the books
        
        User user = (User) model.getAttribute("user");
<<<<<<< HEAD
        if (user!=null){
            Long userid =user.getId();
            Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);
    
            List<String> names = new ArrayList<>();
    
            for (Element book : userBooks.getContent()) {
=======
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

    public static byte[] generatePdf(List<String> content) throws IOException {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfOutputStream));
        Document doc = new Document(pdfDoc);

        for (String name : content) {
            doc.add(new Paragraph(new Text(name)));
        }

        doc.close();
        pdfOutputStream.close();

        return pdfOutputStream.toByteArray();
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
>>>>>>> 4f5faffa14fad2d2002ff73e1ae1e9ffd06c67bf
            }
            return names;
        }else{
            return null;
        }
        
    }
    
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
