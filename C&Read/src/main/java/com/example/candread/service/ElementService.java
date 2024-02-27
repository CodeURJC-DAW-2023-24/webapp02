package com.example.candread.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.Element.Countries;
import com.example.candread.model.Element.Genres;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.repositories.ElementRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ElementService {
    
    @Autowired
    private ElementRepository elementRepository;

    @PostConstruct
    public void insertElement(){
        String name1 = "elemento1";
        String description1 = "descripcionmuymuylarga";
        String author1 = "Autor1";
        String imagen1 = "ImagenEjemplo";

        Types type1 = Types.LIBRO;
        String tipo = type1.name();
        /*Seasons season1 = Seasons.INVIERNO;
        States state1 = States.COMPLETO;
        Countries country1 = Countries.ESPAÑA;

        //Crear ReviewPrevio
        Review review1 = new Review("review1", "descriptionreview1", 4);
        List<Review> reviewsPrueba = new ArrayList<>();
        reviewsPrueba.add(review1);

        List<Genres> generos1 = new ArrayList<>();
        generos1.add(Genres.CIENCIAFICCION);
        generos1.add(Genres.ROMANCE); */

        //Contructor del elemento de prueba
       // Element elementoTest = new Element(name1, description1, 
        //author1, reviewsPrueba, type1, season1, state1, country1, generos1);

        Element elementoTest = new Element(name1, description1, author1, imagen1, tipo);

        //Guardar el elemento de prueba creado
        elementRepository.save(elementoTest);

    }

}
