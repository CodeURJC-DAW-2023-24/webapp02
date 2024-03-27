

package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.dto.UserDTO;
import com.example.candread.model.Element;
import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/user")
public class UserApiController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRep;

    @GetMapping("/")
    public Page<User> getUsers(Pageable pageable) {
        return userRep.findAll(pageable);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        try {
            // se crea un usuario y se le asigna uno o varios roles

            User userPrueba = new User(userDTO.getName(), passwordEncoder.encode(userDTO.getPassword()), "USER");

            userRep.save(userPrueba);
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

        Optional<User> optUser = userRep.findById(id);

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

            userRep.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/edit/{id}/image")
    public ResponseEntity<Object> uploadUserImageById(@PathVariable Long id,
            @RequestParam("perfilImage") MultipartFile perfilImage,
            @RequestParam("bannerImage") MultipartFile bannerImage,
             HttpServletRequest request)
            throws URISyntaxException, IOException {
        Optional<User> optUser = userRep.findById(id);

        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            try {
                // Set the image to the element
                byte[] imageData = perfilImage.getBytes();
                user.setProfileImage(new SerialBlob(imageData));
                byte[] imageDataBanner = bannerImage.getBytes();
                user.setBannerImage(new SerialBlob(imageDataBanner));

                user.setBase64BannerImage((Base64.getEncoder().encodeToString(imageDataBanner)));
                user.setBase64ProfileImage((Base64.getEncoder().encodeToString(imageData)));
                // Save in the database
                userRep.save(user);

                String imageUrl = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}/image")
                        .buildAndExpand(id).toUriString();

                return ResponseEntity.created(new URI(imageUrl)).build();

            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // Element not fount and we return 404 error
            return ResponseEntity.notFound().build();
        }
    }

}
