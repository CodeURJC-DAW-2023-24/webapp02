package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.model.Element;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.PagingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.candread.services.UserService;
import java.util.Base64;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("api/books")
public class BookApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private PagingRepository elementsPaged;

    @Autowired
    private ElementRepository elementRepo;

    @GetMapping("/")
    public Page<Element> getBooks(Pageable pageable) {
        return elementsPaged.findByType("LIBRO", pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Element> getBookById(@PathVariable Long id) {
        Optional<Element> optElement = elementRepo.findById(id);

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBookImageById(@PathVariable Long id) {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            Blob imageBlob = element.getImageFile();

            try {
                if (imageBlob != null && imageBlob.length() > 0) {
                    byte[] imageData = imageBlob.getBytes(1, (int) imageBlob.length());
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
                } else {
                    // Image is not found and we print 404 error
                    return ResponseEntity.notFound().build();
                }
            } catch (SQLException e) {
                // Possible exceptions because of reading the Blob bytes
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            // If the book is not found we print the 404 error
            return ResponseEntity.notFound().build();
        }
    }

    //Tengo que mirar con que formato pasar el imageFile 
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadBookImageById(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile,  HttpServletRequest request) throws URISyntaxException, IOException {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");

        if (optElement.isPresent()) {
            Element element =(Element) optElement.get();
            try {
                // Set the image to the element
                byte[] imageData = imageFile.getBytes();
                element.setImageFile(new SerialBlob(imageData));
                element.setBase64Image(Base64.getEncoder().encodeToString(imageData));
                // Save in the database
                elementRepo.save(element);

                String imageUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}/image").buildAndExpand(id).toUriString();

                return ResponseEntity.created(new URI(imageUrl)).build();
                
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // Element not fount and we return 404 error
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/image") 
    public ResponseEntity<Object> updateBookImage(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
                                                   
        // Verify if the book exists
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element book = optElement.get();
        byte[] imageBytes = imageFile.getBytes();
        // Update the book's image file
        try {
            book.setImageFile(new SerialBlob(imageBytes));
            book.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
            elementRepo.save(book);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteBookImage(@PathVariable Long id){
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        //If the book is not found we return a 404 response
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element book = (Element) optElement.get();
        book.setImageFile(null);
        book.setBase64Image(null);

        elementRepo.save(book);

        return ResponseEntity.noContent().build();
    }

}
