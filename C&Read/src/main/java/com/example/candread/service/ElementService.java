package com.example.candread.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.Element.Countries;
import com.example.candread.model.Element.Genres;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.repositories.ElementRepository;

public class ElementService {
    
    @Autowired
    private ElementRepository elementRepository;

    public void insertElement(){

        String name1 = "ele1";
        String description1 = "descripcionmuymuylarga";
        String author1 = "Autor1";

        Types type1 = Types.LIBRO;
        Seasons season1 = Seasons.INVIERNO;
        States state1 = States.COMPLETO;
        Countries country1 = Countries.ESPAÑA;

        //Crear ReviewPrevio
        Review review1 = new Review("review1", "descriptionreview1", 4);
        List<Review> reviewsPrueba = new ArrayList<>();
        reviewsPrueba.add(review1);

        List<Genres> generos1 = new ArrayList<>();
        generos1.add(Genres.CIENCIAFICCION);
        generos1.add(Genres.ROMANCE);
        

        //Contructor del elemento de prueba
        Element elementoTest = new Element(name1, description1, 
        author1, reviewsPrueba, type1, season1, state1, country1, generos1);

        //Guardar el elemento de prueba creado
        elementRepository.save(elementoTest);

    }


}
