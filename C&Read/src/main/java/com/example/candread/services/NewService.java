package com.example.candread.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.candread.model.New;
import com.example.candread.repositories.NewRepository;

import java.util.List;

@Service
public class NewService {

    @Autowired
    private NewRepository newRepository;

    public void saveNew(New newO) {
        // Guardar la nueva noticia
        if(newO!=null){
            newRepository.save(newO);
        }
        // Obtener todas las noticias
        List<New> allNews = newRepository.findAll();

        // Si hay más de tres noticias, eliminar las más antiguas
        if (allNews.size() > 3) {
            int numberOfNewsToDelete = allNews.size() - 3;
            for (int i = 0; i < numberOfNewsToDelete; i++) {
                New oldestNew = allNews.get(i);
                if(oldestNew!=null){
                    newRepository.delete(oldestNew);
                }
            }
        }
    }
}
