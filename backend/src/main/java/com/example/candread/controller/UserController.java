package com.example.candread.controller;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.candread.model.User;
import com.example.candread.security.SecurityConfiguration;
import com.example.candread.services.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityConfiguration userDetailsService;

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        try {
            // se crea un usuario y se le asigna uno o varios roles
            Blob profileblob = userService.getBlob("static/Images/img-UserProfile2.png");
            Blob bannerblob = userService.getBlob("static/Images/imagenBanner.jpg");

            User userPrueba = new User(user.getName(), passwordEncoder.encode(user.getPassword()), "USER");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);
            //userRepository.save(userPrueba);
            userService.repoSaveUser(userPrueba);

            // userRepository.save(encryptedUser);

            return "redirect:/LogIn";
        } catch (Exception e) {
            return "redirect:/SignIn";
        }
    }

    @PostMapping("/update")
    public String updateUser(Model model, @RequestParam("name") String name,
            @RequestParam(value = "base64ProfileImage", required = false) MultipartFile base64ProfileImage,
            @RequestParam(value = "base64BannerImage", required = false) MultipartFile base64BannerImage) {

        User user = (User) model.getAttribute("user");
        String userCurrentName = user.getName();
        try {
            if (!base64BannerImage.getOriginalFilename().equals("")
                    || !base64ProfileImage.getOriginalFilename().equals("")
                    || !name.equals("")) {

                if (!base64BannerImage.getOriginalFilename().equals("")) {
                    byte[] imageBannerBytes = base64BannerImage.getBytes();
                    Blob profileBannerBlob = new SerialBlob(imageBannerBytes);
                    user.setBannerImage(profileBannerBlob);
                }
                if (!base64ProfileImage.getOriginalFilename().equals("")) {
                    byte[] imageProfileBytes = base64ProfileImage.getBytes();
                    Blob profileImageBlob = new SerialBlob(imageProfileBytes);
                    user.setProfileImage(profileImageBlob);
                }
                if (!name.equals("")) {
                    user.setName(name);
                }
                //userRepository.save(user);
                userService.repoSaveUser(user);
                userCurrentName = user.getName();
                userDetailsService.updateSecurityContext(userService, userCurrentName);
            }
            return "redirect:/" + userCurrentName + "/Profile";
        } catch (Exception e) {
            return "redirect:/" + userCurrentName + "/Profile";
        }
    }

    @PostMapping("/updateLists")
    public String updateUserLists(Model model, @RequestParam("listId") String listId) {

        User user = (User) model.getAttribute("user");
        String userCurrentName = user.getName();
        try {
            String[] parts = listId.split("/");
            String key = parts[0];
            Long id = Long.parseLong(parts[1]);

            Map<String, List<Long>> userLists = user.getListasDeElementos();

            if (userLists.containsKey(key)) {
                List<Long> list = userLists.get(key);
                boolean removed = list.remove(id);
                if (removed) {
                    user.setListasDeElementos(userLists);
                }

            }
            //userRepository.save(user);
            userService.repoSaveUser(user);
            return "redirect:/" + userCurrentName + "/Profile";
        } catch (Exception e) {
            return "redirect:/" + userCurrentName + "/Profile";
        }
    }

    @PostMapping("/updateNewList")
    public String updateUserNewList(Model model,  @RequestParam("name") String name) {

        User user = (User) model.getAttribute("user");
        String userCurrentName = user.getName();
        try {
            List<Long> newList = new ArrayList<>();
            Map<String, List<Long>> userLists = user.getListasDeElementos();
            userLists.put(name, newList);
            user.setListasDeElementos(userLists);
            //userRepository.save(user);
            userService.repoSaveUser(user);
            return "redirect:/" + userCurrentName + "/Profile";
        } catch (Exception e) {
            return "redirect:/" + userCurrentName + "/Profile";
        }
    }
}
