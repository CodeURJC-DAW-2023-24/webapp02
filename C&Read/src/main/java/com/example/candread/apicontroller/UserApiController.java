package com.example.candread.apicontroller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.dto.UserDTO;
import com.example.candread.model.User;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/users")
public class UserApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/")
    public Page<User> getUsers(Pageable pageable) {
        return userService.repoFindAll(pageable);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        try {
            // se crea un usuario y se le asigna uno o varios roles
            User userPrueba = new User(userDTO.getName(), passwordEncoder.encode(userDTO.getPassword()), "USER");
            //We save the user in the DDBB
            userService.repoSaveUser(userPrueba);
            Long userId = userPrueba.getId();
            String userURL = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(userId)
                    .toUriString();
            return ResponseEntity.created(new URI(userURL)).build();
        } catch (Exception e) {
            System.out.print("error:");
        }
        return null;
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updateSerie(@PathVariable Long id,
            @RequestBody UserDTO userDTO,
            HttpServletRequest request) throws URISyntaxException {

        Optional<User> optUser = userService.repoFindById(id);
        
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }
            if (userDTO.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            if (userDTO.getRoles() != null) {
                List<String> newRolesList = userDTO.getRoles();
                List<String> rolesList = user.getRoles();
                for (String rol : newRolesList) {
                    if (!rolesList.contains(rol)) {
                        rolesList.add(rol);
                    }
                }
                user.setRoles(rolesList);
            }
            //We save the user in the DDBB
            userService.repoSaveUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}