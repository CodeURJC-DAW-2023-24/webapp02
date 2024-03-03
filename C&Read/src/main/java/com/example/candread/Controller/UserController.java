package com.example.candread.Controller;

import java.sql.Blob;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.UserService;

import com.example.candread.Security.RepositoryUserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        try {
            // se crea un usuario y se le asigna uno o varios roles
            Blob profileblob = userService.getBlob("static/Images/img-UserProfile2.png");
            Blob bannerblob = userService.getBlob("static/Images/imagenBanner.jpg");

            User userPrueba = new User(user.getName(), passwordEncoder.encode(user.getPassword()), "USER");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);
            userRepository.save(userPrueba);

            // userRepository.save(encryptedUser);

            return "redirect:/LogIn";
        } catch (Exception e) {
            return "redirect:/SignIn";
        }
    }

    @PostMapping("/update")
    public String updateUser(Model model, @RequestParam("name") String name,
            @RequestParam(value = "base64ProfileImage", required = false) MultipartFile base64ProfileImage,
            @RequestParam(value = "base64ProfileImage", required = false) MultipartFile base64BannerImage) {

        try {
            User user = (User) model.getAttribute("user");
            String n = name;
            if (base64BannerImage != null && base64ProfileImage != null) {

                byte[] imageProfileBytes = base64ProfileImage.getBytes();
                byte[] imageBannerBytes = base64ProfileImage.getBytes();

                Blob profileImageBlob = new SerialBlob(imageProfileBytes);
                Blob profileBannerBlob = new SerialBlob(imageBannerBytes);

                user.setBannerImage(profileBannerBlob);
                user.setProfileImage(profileImageBlob);
                user.setName(name);
                userRepository.save(user);
            }
            return "redirect:/" + name + "/Profile";
        } catch (Exception e) {
            return "redirect:/" + name + "/Profile";
        }

    }

}
