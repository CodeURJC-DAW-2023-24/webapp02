package com.example.candread.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candread.model.User;
import com.example.candread.repositories.UserPagingRepository;
import com.example.candread.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPagingRepository userPagedRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ElementService elementService;

    

    @PostConstruct
    public void insertUsers() throws Exception {


        Blob profileblob = getBlob("static/Images/Img-UserProfile2.png");
        Blob bannerblob = getBlob("static/Images/imagenBanner.jpg");

        //BASE USERS IN THE SYSTEM: 1 ADMIN 1 USER 1 ADMIN-USER
        if(!existsByUsernameAndPassword("admin1", "pass")){
            User userPrueba = new User("admin1", passwordEncoder.encode("pass"), "USER", "ADMIN");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);
            Map<String, List<Long>> listaE = new HashMap<>();
            List<Long> idEl = Arrays.asList(1L, 2L, 3L);
            listaE.put("Favoritos", idEl);
            listaE.put("Viendo", idEl);
            userPrueba.setListasDeElementos(listaE);
            userRepository.save(userPrueba);
        }
        if(!existsByUsernameAndPassword("admin3", "123")){
            User userPrueba = new User("admin3", passwordEncoder.encode("123"),  "ADMIN");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);
            userRepository.save(userPrueba);
        }

        if(!existsByUsernameAndPassword("Antonio27", "pass")){
            User userPrueba = new User("Antonio27", passwordEncoder.encode("pass"), "USER");
            userPrueba.setBannerImage(bannerblob);
            userPrueba.setProfileImage(profileblob);
            userRepository.save(userPrueba);

        }

        //ElementService created to execute after users creation
        elementService.insertElement();
        elementService.insertSeries();
        elementService.inserFilms();
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userPagedRepository.findAll(pageable);
    }
    
    public boolean existsByUsernameAndPassword(String username, String password) {
        return userRepository.findByName(username)
        .map(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(false);
    }

    public Blob getBlob(String path) throws IOException, SerialException, SQLException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
    
        Resource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }
    
        byte[] imageBytes = outputStream.toByteArray();
        Blob blobi = new SerialBlob(imageBytes);
        return blobi;
    }

        public void fullSet64Image() throws SQLException, IOException {
        List<User> users = userRepository.findAll();
        int size = users.size();
        long longSize = size;
        for (long i = 1; i <= longSize; i++) {
            setUsersImage64(i);
        }
    }

    public void setUsersImage64(long id) throws SQLException, IOException {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow();
        Blob blob = user.getProfileImage();
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        user.setBase64ProfileImage(base64Image);

        blob = user.getBannerImage();
        inputStream = blob.getBinaryStream();
        imageBytes = inputStream.readAllBytes();
        base64Image = Base64.getEncoder().encodeToString(imageBytes);
        user.setBase64BannerImage(base64Image);
        
        inputStream.close();
   }

   public void repoSaveUser(User userToSave){
    userRepository.save(userToSave);
   }
}
