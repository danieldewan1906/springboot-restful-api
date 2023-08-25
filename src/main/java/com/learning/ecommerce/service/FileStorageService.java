package com.learning.ecommerce.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.learning.ecommerce.FileStorageProperties;
import com.learning.ecommerce.exception.FileNotFoundExceptions;
import com.learning.ecommerce.exception.FileStorageException;

@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties prop) {
        this.fileStorageLocation = Paths.get(prop.getUploadDir());
    }

    public void init(){
        try {
            Files.createDirectory(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("Gagal membuat direktori", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // String filenameExtension = StringUtils.getFilenameExtension(StringUtils.cleanPath(file.getOriginalFilename()));
            // String fileName = UUID.randomUUID().toString() + "." + filenameExtension;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Gagal Menyimpan File", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new FileNotFoundExceptions("File Tidak Ditemukkan");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new FileNotFoundExceptions("File Tidak Ditemukkan", e);
        }
    }
}
