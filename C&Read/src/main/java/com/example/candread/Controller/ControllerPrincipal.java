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
import com.example.candread.services.UserService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

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
    public ResponseEntity<byte[]> downloadNames(Model model, HttpSession session, Pageable pageable)
            throws IOException {
        List<String> names = obtenerNombresDeLibros(model, pageable);

        byte[] pdfBytes = generatePdf(names);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "nombres_de_libros.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private List<String> obtenerNombresDeLibros(Model model, Pageable pageable) {
        // Logic to get the names of the books

        User user = (User) model.getAttribute("user");
        List<String> names = new ArrayList<>();
        if (user != null) {
            Long userid = user.getId();
            Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);

            for (Element book : userBooks.getContent()) {
                names.add(book.getName());
            }
        }
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

    @GetMapping("/Admin")
    public String moveToAdmin(Model model) throws SQLException, IOException {
        userService.fullSet64Image();
        ;
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
