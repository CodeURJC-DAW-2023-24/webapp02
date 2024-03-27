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

import com.example.candread.dto.ElementDTO;
import com.example.candread.dto.UpdateBookImageDTO;
import com.example.candread.model.Element;
import com.example.candread.model.Element.Countries;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.PagingRepository;
import com.example.candread.services.ElementService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/books")
public class BookApiController {

    @Autowired
    private PagingRepository elementsPaged;

    @Autowired
    private ElementService elementService;

    @GetMapping("/")
    public Page<Element> getBooks(Pageable pageable) {
        return elementsPaged.findByType("LIBRO", pageable);
    }

    @GetMapping("/top")
    public Page<Element> getTop5Books(Pageable pageable) {
        return elementRepo.findTopElementsByRating("LIBRO", pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Element> getBookById(@PathVariable Long id) {
        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id,
            @RequestBody ElementDTO elementDTO,
            HttpServletRequest request) throws URISyntaxException {

        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            if (elementDTO.getName() != null) {
                element.setName(elementDTO.getName());
            }
            if (elementDTO.getDescription() != null) {
                element.setDescription(elementDTO.getDescription());
            }
            if (elementDTO.getAuthor() != null) {
                element.setAuthor(elementDTO.getAuthor());
            }
            if (elementDTO.getYear() != 0) {
                element.setYear(elementDTO.getYear());
            }
            if (elementDTO.getSeason()!= null) {
                Seasons s = Seasons.valueOf(elementDTO.getSeason());
                element.setSeason(s);
            }
            if (elementDTO.getState() != null) {
                States s = States.valueOf(elementDTO.getState());
                element.setState(s);
            }

            if (elementDTO.getType() != null) {
                Types t = Types.valueOf(elementDTO.getType());
                element.setType(t);
            }

            if (elementDTO.getCountry() != null) {
                Countries c = Countries.valueOf(elementDTO.getCountry());
                element.setCountry(c);
            }
            if (elementDTO.getGenres() != null) {
                List<String> newGenresList = elementDTO.getGenres();
                List<String> genreList = element.getGeneros();
                for (String genero : newGenresList) {
                    if (!genreList.contains(genero)) {
                        genreList.add(genero);
                    }
                }
                element.setGeneros(genreList);
            }

            //elementRepo.save(element);
            elementService.repoSaveElement(element);
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> uploadBook(@RequestBody ElementDTO elementDTO,
            HttpServletRequest request) throws URISyntaxException {

        List<String> genresList = elementDTO.getGenres();
        Element element = new Element(elementDTO.getName(), elementDTO.getDescription(), elementDTO.getAuthor(),
            elementDTO.getType(), elementDTO.getSeason(), elementDTO.getState(), elementDTO.getCountry(), genresList, elementDTO.getYear());
        //elementRepo.save(element);
        elementService.repoSaveElement(element);
        Long bookId = element.getId();
        String bookUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(bookId)
                .toUriString();

        return ResponseEntity.created(new URI(bookUrl)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {

        //elementRepo.deleteById(id);
        elementService.repoDeleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBookImageById(@PathVariable Long id) {
        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");
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

    


    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadBookImageById(@PathVariable Long id,
            @RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request)
            throws URISyntaxException, IOException {
        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");
        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            try {
                // Set the image to the element
                byte[] imageData = imageFile.getBytes();
                element.setImageFile(new SerialBlob(imageData));
                element.setBase64Image(Base64.getEncoder().encodeToString(imageData));
                // Save in the database
                //elementRepo.save(element);
                elementService.repoSaveElement(element);

                String imageUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id).toUriString();

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
    public ResponseEntity<Object> updateBookImage(@PathVariable Long id,
        @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // Verify if the book exists
        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element book = optElement.get();
        byte[] imageBytes = imageFile.getBytes();
        // Update the book's image file
        try {
            book.setImageFile(new SerialBlob(imageBytes));
            book.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
            //elementRepo.save(book);
            elementService.repoSaveElement(book);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    
    
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteBookImage(@PathVariable Long id) {
        //Optional<Element> optElement = elementRepo.findByIdAndType(id, "LIBRO");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "LIBRO");
        // If the book is not found we return a 404 response
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element book = (Element) optElement.get();
        book.setImageFile(null);
        book.setBase64Image(null);

        //elementRepo.save(book);
        elementService.repoSaveElement(book);

        return ResponseEntity.noContent().build();
    }

}
