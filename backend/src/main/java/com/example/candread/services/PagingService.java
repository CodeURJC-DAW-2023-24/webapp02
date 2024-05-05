package com.example.candread.services;

import org.springframework.data.domain.Page;
import com.example.candread.model.Element;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.candread.repositories.PagingRepository;

@Service
public class PagingService {
    
    @Autowired
    private PagingRepository pagingRepository;

    public Page<Element> repoFindByType(String type, Pageable pageable) {
        return pagingRepository.findByType(type, pageable);
    }

    public Page<Element> repoFindByTypeAndGenres(String type, String genre, Pageable pageable){
        return pagingRepository.findByTypeAndGenres(type, genre, pageable);
    }

    public Page<Element> repoFindByTypeAndSeason(String type, String season, Pageable pageable){
        return pagingRepository.findByTypeAndSeason(type, season, pageable);
    }

    public Page<Element> repoFindByTypeAndCountry(String type, String country, Pageable pageable){
        return pagingRepository.findByTypeAndCountry(type, country, pageable);
    }

    public Page<Element> repoFindByTypeAndState(String type,String state, Pageable pageable){
        return pagingRepository.findByTypeAndState(type, state, pageable);
    }

    public Page<Element> repoFindByUsersIdAndType(Long userid, String type, Pageable pageable){
        return pagingRepository.findByUsersIdAndType(userid, type, pageable);
    }

    public Page<Element> repoFindByUsersFavouritedIdAndType(Long userid, String type, Pageable pageable){
        return pagingRepository.findByUsersFavouritedIdAndType(userid, type, pageable);
    }

    public Page<Element> repoFindByTypeAndRecommendations(String type,List<Element> recomendation, Pageable pageable){
        return pagingRepository.findByTypeAndRecommendations(type, recomendation, pageable);
    }

    

}
