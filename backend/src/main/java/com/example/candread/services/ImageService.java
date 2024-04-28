package com.example.candread.services;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class ImageService {

   
    public Blob createBlobFromLocalFile(String filePath) throws IOException, SQLException {
        File file = new File(filePath);

        
        if (!file.exists()) {
            throw new IOException("El archivo no existe en la ruta proporcionada: " + filePath);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] data = FileCopyUtils.copyToByteArray(inputStream); 

           
            Blob blob = new SerialBlob(data);
            return blob;
        }
    }


    public Blob createBlobFromMultipartFile(MultipartFile multipartFile) throws IOException, SQLException {
        byte[] data = multipartFile.getBytes();

        Blob blob = new SerialBlob(data);
        return blob;
    }
}
