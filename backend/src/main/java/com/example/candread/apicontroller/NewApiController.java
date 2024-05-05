package com.example.candread.apicontroller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.model.New;
import com.example.candread.services.NewService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/news")
public class NewApiController {

    @Autowired
    private NewService newService;

    @GetMapping("/fiveRecentNews")
    public List<New> get5RecentNews(Pageable pageable) {
        return newService.repoFindFirst5();
    }

    @PostMapping("/")
    public ResponseEntity<New> postMethodName(@RequestBody New new1, HttpServletRequest request) {
        try {
            newService.repoSaveNew(new1);
            Long newId = new1.getId();
            String newURL = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(newId)
                    .toUriString();
            return ResponseEntity.created(new URI(newURL)).body(new1);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
