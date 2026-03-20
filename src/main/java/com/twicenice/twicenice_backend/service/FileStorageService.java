package com.twicenice.twicenice_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

   public String storeFile(MultipartFile file) throws IOException {
    System.out.println("=== CLOUDINARY UPLOAD START ===");
    System.out.println("Cloud name: " + cloudinary.config.cloudName);
    
    Map uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap("folder", "twicenice")
    );
    
    String secureUrl = (String) uploadResult.get("secure_url");
    System.out.println("=== CLOUDINARY RESULT ===");
    System.out.println("secure_url: " + secureUrl);
    System.out.println("All keys: " + uploadResult.keySet());
    
    return secureUrl;
}
}
