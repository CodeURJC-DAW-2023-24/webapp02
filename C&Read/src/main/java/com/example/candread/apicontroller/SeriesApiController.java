package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.model.Element;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/series")
public class SeriesApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private PagingRepository elementsPaged;

    @Autowired
    private ElementRepository elementRepo;

    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<Page<Element>> getBooks(@RequestParam("page") Optional<Integer> page, Pageable pageable) {
        int pageNumber = page.orElse(0);
        int pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize);
        
        return ResponseEntity.ok(elementsPaged.findByType("SERIE", pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Element> getSeriesById(@PathVariable Long id) {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getSerieImageById(@PathVariable Long id) {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            Blob imageBlob = element.getImageFile();

            try {
                if (imageBlob != null && imageBlob.length() > 0) {
                    byte[] imageData = imageBlob.getBytes(1, (int) imageBlob.length());
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadSerieImageById(@PathVariable Long id,
            @RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request)
            throws URISyntaxException, IOException {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            try {
                // Set the image to the element
                byte[] imageData = imageFile.getBytes();
                element.setImageFile(new SerialBlob(imageData));
                element.setBase64Image(Base64.getEncoder().encodeToString(imageData));
                // Save in the database
                elementRepo.save(element);

                String imageUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}/image")
                        .buildAndExpand(id).toUriString();

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
    public ResponseEntity<Object> updateSerieImage(@PathVariable Long id,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element serie = optElement.get();
        byte[] imageBytes = imageFile.getBytes();
        try {
            serie.setImageFile(new SerialBlob(imageBytes));
            serie.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
            elementRepo.save(serie);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteSerieImage(@PathVariable Long id) {
        Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element serie = (Element) optElement.get();
        serie.setImageFile(null);
        serie.setBase64Image(null);

        elementRepo.save(serie);

        return ResponseEntity.noContent().build();
    }

}
