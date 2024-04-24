package com.example.candread.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.candread.model.New;
import com.example.candread.services.NewService;


@RestController
@RequestMapping("api/news")
public class NewApiController {


    @Autowired
    private NewService newService;

    @GetMapping("/fiveRecentNews")
    public List<New> get5RecentNews(Pageable pageable) {
        List<New> n = newService.repoFindFirst5();
        return newService.repoFindFirst5();
    }
    

}
