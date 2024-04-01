package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.candread.dto.UserDTO;
import com.example.candread.model.Element;
import com.example.candread.model.User;
import com.example.candread.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;


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

    @PostMapping("/")
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getSpecificUser(@PathVariable Long id) {

        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            userService.repoSaveUser(user);
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.notFound().build(); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        //elementRepo.deleteById(id);
        userService.repoDeleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        Optional<User> optUser = userService.repoFindById(id);

        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            Blob userImage = user.getProfileImage();

            try {
                if (userImage != null && userImage.length() > 0) {
                    byte[] imageData = userImage.getBytes(1, (int) userImage.length());
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
                } else {
                    // Image is not found and we print 404 error
                    return ResponseEntity.notFound().build();
                }
            } catch (SQLException e) {
                // Possible exceptions because of reading the Blob bytes
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadUserImage(@PathVariable Long id,
            @RequestParam("profileImage") MultipartFile imageFile, HttpServletRequest request)
            throws URISyntaxException, IOException {
        
        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            try {
                // Set the image to the user
                byte[] imageData = imageFile.getBytes();
                user.setProfileImage(new SerialBlob(imageData));
                user.setBase64ProfileImage(Base64.getEncoder().encodeToString(imageData));

                // Save in the database
                userService.repoSaveUser(user);

                String imageUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id).toUriString();

                return ResponseEntity.created(new URI(imageUrl)).build();

            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // User not fount and we return 404 error
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> updateUserImage(@PathVariable Long id,
        @RequestParam("profileImage") MultipartFile imageFile) throws IOException {

        // Verify if the user exists
        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = (User) optUser.get();
        byte[] imageBytes = imageFile.getBytes();
       
        try {
            user.setProfileImage(new SerialBlob(imageBytes));
            user.setBase64ProfileImage(Base64.getEncoder().encodeToString(imageBytes));
            
            userService.repoSaveUser(user);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteUserImage(@PathVariable Long id) {
        
        Optional<User> optUser = userService.repoFindById(id);
        // If the user is not found we return a 404 response
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = (User) optUser.get();
        user.setProfileImage(null);
        user.setBase64ProfileImage(null);

        userService.repoSaveUser(user);

        return ResponseEntity.noContent().build();
    }
    
}