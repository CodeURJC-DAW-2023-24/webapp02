package com.example.candread.apicontroller;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.candread.model.Element;
import com.example.candread.model.Element.Genres;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.services.ElementService;


@RestController
@RequestMapping("api/elements")
public class ElementApiController {


    @Autowired
    private ElementService elementService;

    @GetMapping("/all")
    public List<Element> getAllElements(Pageable pageable) {
        return elementService.repoFindAll();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getElementImageById(@PathVariable Long id) {
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindById(id);
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

    @GetMapping("/fiveNewElements")
    public List<Element> get5NeweElements(Pageable pageable) {
        return elementService.repoFind5NewElements();
    }
    
    @GetMapping("/genres")
    public Genres[] getGenres(Pageable pageable) {
        Genres[] genres = Element.Genres.values();
        return genres;
    }

    @GetMapping("/types")
    public Types[] getTypes(Pageable pageable) {
        Types[] types = Element.Types.values();
        return types;
    }

    @GetMapping("/states")
    public States[] getState(Pageable pageable) {
        States[] states = Element.States.values();
        return states;
    }

    @GetMapping("/season")
    public Seasons[] getSeason(Pageable pageable) {
        Seasons[] season = Element.Seasons.values();
        return season;
    }

}
