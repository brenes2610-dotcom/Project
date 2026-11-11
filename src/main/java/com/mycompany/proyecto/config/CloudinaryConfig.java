/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.config;

/**
 *
 * @author jurge
 */
import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name",
                System.getenv("CLOUDINARY_CLOUD_NAME"));

        config.put("api_key",
                System.getenv("CLOUDINARY_API_KEY"));

        config.put("api_secret",
                System.getenv("CLOUDINARY_API_SECRET"));

        config.put("secure", true);

        return new Cloudinary(config);
    }
}
