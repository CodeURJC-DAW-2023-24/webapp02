package com.example.candread.service;
import java.net.MalformedURLException;
import java.net.URL;
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

        List<String> generosEjemplo1 = new ArrayList<>();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.ROMANCE.name());

        String imagen2 = new String("static/Images/Alas_Sangre.jpg");
        
        
        
        //CONSTRUCTURES DE DATOS BASE EN BASE DE DATOS:
        Element elementoTest1 = new Element("Alas de Sangre", "Vuela... o muere. El nuevo fenómeno de fantasía juvenil del que todo el mundo habla.",
        "Rebecca Yarros", "https://m.media-amazon.com/images/I/91OI4F8Fa7L._AC_UF894,1000_QL80_.jpg", Types.LIBRO.name(), Seasons.OTOÑO.name(), States.COMPLETO.name(), 
        Countries.ESTADOSUNIDOS.name(), generosEjemplo1);

        //Guardar el elemento de prueba creado
        elementRepository.save(elementoTest1);

        /* 
        //Crear ReviewPrevio
        Review review1 = new Review("review1", "descriptionreview1", 4);
        List<Review> reviewsPrueba = new ArrayList<>();
        reviewsPrueba.add(review1);
 */
    }

}
