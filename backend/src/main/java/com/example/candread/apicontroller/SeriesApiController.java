package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.dto.ElementDTO;
import com.example.candread.model.Element;
import com.example.candread.model.Element.Countries;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.services.ElementService;
import com.example.candread.services.PagingService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/series")
public class SeriesApiController {

    @Autowired
    private PagingService elementsPaged;

    @Autowired
    private ElementService elementService;

    @ResponseBody
    @GetMapping("/")
    public Page<Element> getSeries(Pageable pageable) {
        return elementsPaged.repoFindByType("SERIE", pageable);
    }

    @GetMapping("/genre")
    public Page<Element> getSeriesByGenre(@RequestParam(required = false) String filter, Pageable pageable) {
        if (filter != null) {
            return elementsPaged.repoFindByTypeAndGenres("SERIE", filter, pageable);
        }
        return null;
    }

    @GetMapping("/season")
    public Page<Element> getSeriesBySeason(@RequestParam(required = false) String filter, Pageable pageable) {
        if (filter != null) {
            return elementsPaged.repoFindByTypeAndSeason("SERIE", filter, pageable);
        }
        return null;
    }

    @GetMapping("/country")
    public Page<Element> getSeriesByCountry(@RequestParam(required = false) String filter, Pageable pageable) {
        if (filter != null) {
            return elementsPaged.repoFindByTypeAndCountry("SERIE", filter, pageable);
        }
        return null;
    }

    @GetMapping("/state")
    public Page<Element> getSeriesByState(@RequestParam(required = false) String filter, Pageable pageable) {
        if (filter != null) {
            return elementsPaged.repoFindByTypeAndState("SERIE", filter, pageable);
        }
        return null;
    }

    @GetMapping("/top")
    public Page<Element> getTop5Series(Pageable pageable) {
        return elementService.repofindTopElementsByRating("SERIE", pageable);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SERIE ID CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Element> getSeriesById(@PathVariable Long id) {
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SERIE PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSerie(@PathVariable Long id,
            @RequestBody ElementDTO elementDTO,
            HttpServletRequest request) throws URISyntaxException {

        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");
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
            if (elementDTO.getSeason() != null) {
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

            // elementRepo.save(element);
            elementService.repoSaveElement(element);
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SERIE POST CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Object> uploadSerie(@RequestBody ElementDTO elementDTO,
            HttpServletRequest request) throws URISyntaxException {

        List<String> genresList = elementDTO.getGenres();
        Element element = new Element(elementDTO.getName(), elementDTO.getDescription(), elementDTO.getAuthor(),
                elementDTO.getType(), elementDTO.getSeason(), elementDTO.getState(), elementDTO.getCountry(),
                genresList, elementDTO.getYear());
        // elementRepo.save(element);
        elementService.repoSaveElement(element);
        Long bookId = element.getId();
        String bookUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(bookId)
                .toUriString();

        return ResponseEntity.created(new URI(bookUrl)).build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SERIE DELETE CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSerie(@PathVariable Long id) {

        // elementRepo.deleteById(id);
        elementService.repoDeleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SERIE IMAGE ID CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getSerieImageById(@PathVariable Long id) {
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SERIE IMAGE PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadSerieImageById(@PathVariable Long id,
            @RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request)
            throws URISyntaxException, IOException {
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");
        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            try {
                // Set the image to the element
                byte[] imageData = imageFile.getBytes();
                element.setImageFile(new SerialBlob(imageData));
                element.setBase64Image(Base64.getEncoder().encodeToString(imageData));
                // Save in the database
                // elementRepo.save(element);
                elementService.repoSaveElement(element);

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SERIE IMAGE PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> updateSerieImage(@PathVariable Long id,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // Verify if the book exists
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element serie = optElement.get();
        byte[] imageBytes = imageFile.getBytes();
        // Update the book's image file
        try {
            serie.setImageFile(new SerialBlob(imageBytes));
            serie.setBase64Image(Base64.getEncoder().encodeToString(imageBytes));
            // elementRepo.save(serie);
            elementService.repoSaveElement(serie);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SERIE IMAGE DELETE CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serie not found", content = @Content)
    })
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteSerieImage(@PathVariable Long id) {
        // Optional<Element> optElement = elementRepo.findByIdAndType(id, "SERIE");
        Optional<Element> optElement = elementService.repoFindByIdAndType(id, "SERIE");
        if (optElement.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Element serie = (Element) optElement.get();
        serie.setImageFile(null);
        serie.setBase64Image(null);

        // elementRepo.save(serie);
        elementService.repoSaveElement(serie);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{name}/")
    public ResponseEntity<Element> getSerieByName(@PathVariable String name) {
        Optional<Element> optElement = elementService.repoFindByNameAndType(name,"SERIE");

        if (optElement.isPresent()) {
            Element element = (Element) optElement.get();
            return ResponseEntity.ok(element);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
