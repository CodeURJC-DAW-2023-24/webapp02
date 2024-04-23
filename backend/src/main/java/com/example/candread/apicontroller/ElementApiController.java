package com.example.candread.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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
