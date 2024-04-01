package com.example.candread.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.candread.services.ElementService;
import com.example.candread.services.NewService;
import com.example.candread.services.PagingService;
import com.example.candread.services.UserService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ControllerPrincipal {

    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private NewService newService;

    @Autowired
    private PagingService pagingService;

    // Moverse al main, es la pagina principal y la primera que sale al entrar
    @GetMapping("/")
    public String moveToMain(Model model, HttpServletRequest request) throws SQLException, IOException {
        userService.fullSet64Image();
        elementService.fullSet64Image();

        // CAROUSEL IMG
        //List<Element> elementsRelease = elementRepository.findTop4ByOrderByIdDesc();
        List<Element> elementsRelease = elementService.repofindTop4ByOrderByIdDesc();
        for (int i = 0; i < elementsRelease.size(); i++) {
            Element e = elementsRelease.get(i);
            String img = e.getBase64Image();
            String finalImg = "data:image/jpg;base64," + img;
            model.addAttribute("firstSlide" + i, finalImg);
        }

        // Pasamos los datos a la vista
        //List<New> newsList = newRepository.findAll(); // Obtener todas las noticias
        List<New> newsList = newService.repoFindAll();

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
            throws IOException, SQLException {
        List<Map<String, byte[]>> namesAndImages = obtainNamesAndImagesOfBooks(model, pageable);

        byte[] pdfBytes = generatePdf(namesAndImages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "mis_libros.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private List<Map<String, byte[]>> obtainNamesAndImagesOfBooks(Model model, Pageable pageable) throws SQLException, IOException {
        // Logic to get the names of the books

        User user = (User) model.getAttribute("user");

        //List of maps with the name of the element as key an the image as value
        List<Map<String, byte[]>> elementsWithImages = new ArrayList<>();

        if (user != null) {
            Long userid = user.getId();
            //Page<Element> userBooks = pagingRepository.findByUsersIdAndType(userid, "LIBRO", pageable);
            Page<Element> userBooks = pagingService.repoFindByUsersIdAndType(userid, "LIBRO", pageable);

            for (Element book : userBooks.getContent()) {
                Map<String, byte[]> elementMap = new HashMap<>();
                elementMap.put(book.getName(), getImageBytes(book.getImageFile()));
                elementsWithImages.add(elementMap);
            }
        }
        return elementsWithImages;
    }

    private byte[] getImageBytes(Blob blob) throws SQLException, IOException {
        // Get the byte[] of the blob
        return blob.getBytes(1, (int) blob.length());
    }

    public static byte[] generatePdf(List<Map<String, byte[]>> namesAndImages) throws IOException {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfOutputStream));
        Document doc = new Document(pdfDoc);

        Paragraph title = new Paragraph("Lista de mis Libros")
                        .setFontColor(new DeviceRgb(0, 0, 0)) // Text color: black
                        .setBold() 
                        .setFontSize(20) 
                        .setUnderline()
                        .setTextAlignment(TextAlignment.CENTER) 
                        .setVerticalAlignment(VerticalAlignment.MIDDLE); 

        // Adding a title to the document
        doc.add(title);

        //Spaces between the title and the first image
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("\n"));

        for (Map<String, byte[]> elementMap : namesAndImages) {
            for (Map.Entry<String, byte[]> entry : elementMap.entrySet()) {
                String name = entry.getKey();
                byte[] imageData = entry.getValue();
                // Add the image to the PDF
                Image image = new Image(ImageDataFactory.create(imageData));
                image.scaleToFit(300, 300);
                image.setHorizontalAlignment(HorizontalAlignment.CENTER);
                doc.add(image);

                // Add the name of the element to the PDF
                Paragraph nameParagraph = new Paragraph(name)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
                doc.add(nameParagraph);
                doc.add(new Paragraph("\n"));
            }
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
