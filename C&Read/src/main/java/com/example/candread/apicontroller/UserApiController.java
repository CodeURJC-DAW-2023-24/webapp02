package com.example.candread.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.UserService;

@RestController
@RequestMapping("api/user")
public class UserApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRep;
    
    //va mal
    @GetMapping("/")
    public Page<User> getUsers(Pageable pageable) {
        return userRep.findAll(pageable);
    }
}
