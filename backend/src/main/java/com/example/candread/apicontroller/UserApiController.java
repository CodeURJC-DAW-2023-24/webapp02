package com.example.candread.apicontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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
            userPrueba.setListasDeElementos(userDTO.getListasDeElementos());
            Blob profileblob = userService.getBlob("static/Images/Aladdin.jpg");
            Blob bannerblob = userService.getBlob("static/Images/imagenBanner.jpg");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);

            // We save the user in the DDBB
            userService.repoSaveUser(userPrueba);
            Long userId = userPrueba.getId();
            String userURL = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}").buildAndExpand(userId)
                    .toUriString();
            return ResponseEntity.created(new URI(userURL)).body(userPrueba);
        } catch (Exception e) {
            System.out.print("error:");
        }
        return null;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER ID CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })

    // get the current user
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {

            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            Optional<User> optUser = userService.repoFindByNameAndRoles(username, roles);

            User user = (User) optUser.get();

            return ResponseEntity.ok(user);
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getSpecificUser(@PathVariable Long id) {

        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            userService.repoSaveUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
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
            Map<String, List<Long>> mapaPrueba = new HashMap<>();
            if (userDTO.getListasDeElementos() != null && userDTO.getListasDeElementos().size() != 0) {
                Map<String, List<Long>> newlistOfList = userDTO.getListasDeElementos();
                Map<String, List<Long>> listOfList = user.getListasDeElementos();
                for (Map.Entry<String, List<Long>> entry : newlistOfList.entrySet()) {
                    String key = entry.getKey();
                    List<Long> sourceList = entry.getValue();
                    List<Long> targetList = listOfList.getOrDefault(key, new ArrayList<>());
                    for (Long element : sourceList) {
                        if (!targetList.contains(element)) {
                            targetList.add(element);
                        }
                    }
                    listOfList.put(key, targetList);
                }
                user.setListasDeElementos(listOfList);
            }
            // We save the user in the DDBB
            User userResponse = userService.repoSaveUser(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER DELETE CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        // elementRepo.deleteById(id);
        userService.repoDeleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER ID CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER image not found", content = @Content)
    })
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "USER IMAGE POST CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadUserImage(@PathVariable Long id,
            @RequestParam(value = "profileImage", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            HttpServletRequest request) throws URISyntaxException, IOException, SerialException, SQLException {

        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            if (imageFile != null) {
                try {
                    // Set the image to the user
                    byte[] imageData = imageFile.getBytes();
                    user.setProfileImage(new SerialBlob(imageData));
                    user.setBase64ProfileImage(Base64.getEncoder().encodeToString(imageData));

                    // Save in the database
                    userService.repoSaveUser(user);

                    String imageFileUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id)
                            .toUriString();

                    return ResponseEntity.created(new URI(imageFileUrl)).build();

                } catch (SQLException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else if (imageUrl != null) {
                Blob blobAladdin = userService.getBlob(imageUrl);
                user.setProfileImage(blobAladdin);
                user.setBase64ProfileImage(imageUrl);
                userService.repoSaveUser(user);
                String imageFileUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id)
                        .toUriString();
                return ResponseEntity.created(new URI(imageFileUrl)).build();
            } else {
                return ResponseEntity.badRequest().body("Either profileImage or imageUrl parameter is required.");
            }
        } else {
            // User not fount and we return 404 error
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER IMAGE PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
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

        User userResponse = new User();
        try {
            user.setProfileImage(new SerialBlob(imageBytes));
            user.setBase64ProfileImage(Base64.getEncoder().encodeToString(imageBytes));

            userResponse = userService.repoSaveUser(user);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userResponse);
        // return ResponseEntity.noContent().build(userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "USER IMAGE DELETE CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER ID CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acción prohibida", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER BANNER image not found", content = @Content)
    })
    @GetMapping("/{id}/bannerimage")
    public ResponseEntity<byte[]> getUserBannerImage(@PathVariable Long id) {
        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            // Blob userImage = user.getProfileImage();
            Blob userBannerImage = user.getBannerImage();
            try {
                // if (userImage != null && userImage.length() > 0) {
                if (userBannerImage != null && userBannerImage.length() > 0) {
                    byte[] imageData = userBannerImage.getBytes(1, (int) userBannerImage.length());
                    // byte[] imageData = userImage.getBytes(1, (int) userImage.length());
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/bannerimage")
    public ResponseEntity<Object> uploadUserBannerImage(@PathVariable Long id,
            @RequestParam(value = "profileBannerImage", required = false) MultipartFile bannerFile,
            @RequestParam(value = "bannerImageURL", required = false) String bannerImageURL,
            HttpServletRequest request) throws URISyntaxException, IOException, SerialException, SQLException {

        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isPresent()) {
            User user = (User) optUser.get();
            if (bannerFile != null) {
                try {
                    // Set the image to the user
                    byte[] imageData = bannerFile.getBytes();
                    user.setBannerImage(new SerialBlob(imageData));
                    user.setBase64BannerImage(Base64.getEncoder().encodeToString(imageData));

                    // Save in the database
                    userService.repoSaveUser(user);

                    String imageFileUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id)
                            .toUriString();

                    return ResponseEntity.created(new URI(imageFileUrl)).build();

                } catch (SQLException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else if (bannerImageURL != null) {
                Blob blob = userService.getBlob(bannerImageURL);
                user.setBannerImage(blob);
                user.setBase64BannerImage(bannerImageURL);
                userService.repoSaveUser(user);
                String imageFileUrl = ServletUriComponentsBuilder.fromRequestUri(request).buildAndExpand(id)
                        .toUriString();
                return ResponseEntity.created(new URI(imageFileUrl)).build();
            } else {
                return ResponseEntity.badRequest().body("Either bannerImage or bannerUrl parameter is required.");
            }
        } else {
            // User not fount and we return 404 error
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "USER IMAGE PUT CORRECT", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Element.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "USER not found", content = @Content)
    })
    @PutMapping("/{id}/bannerimage")
    public ResponseEntity<Object> updateUserBannerImage(@PathVariable Long id,
            @RequestParam("bannerImage") MultipartFile imageFile) throws IOException {

        // Verify if the user exists
        Optional<User> optUser = userService.repoFindById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = (User) optUser.get();
        byte[] imageBytes = imageFile.getBytes();

        User userResponse = new User();
        try {
            // user.setProfileImage(new SerialBlob(imageBytes));
            user.setBannerImage(new SerialBlob(imageBytes));
            // user.setBase64ProfileImage(Base64.getEncoder().encodeToString(imageBytes));
            user.setBase64BannerImage(Base64.getEncoder().encodeToString(imageBytes));

            userResponse = userService.repoSaveUser(user);

        } catch (SerialException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userResponse);
        // return ResponseEntity.noContent().build(userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}/bannerimage")
    public ResponseEntity<Object> deleteUserBannerImage(@PathVariable Long id) {

        Optional<User> optUser = userService.repoFindById(id);
        // If the user is not found we return a 404 response
        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = (User) optUser.get();
        // user.setProfileImage(null);
        user.setBannerImage(null);
        user.setBase64BannerImage(null);

        userService.repoSaveUser(user);

        return ResponseEntity.noContent().build();
    }
}