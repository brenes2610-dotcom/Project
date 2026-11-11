/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jurge
 */

package com.mycompany.proyecto.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagen(MultipartFile archivo)
            throws IOException {

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una imagen");
        }

        String tipo = archivo.getContentType();

        if (tipo == null ||
                (!tipo.equals("image/jpeg")
                && !tipo.equals("image/png")
                && !tipo.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Solo se permiten imágenes JPG, PNG o WEBP");
        }

        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "La imagen no debe superar los 5 MB");
        }

        Map resultado = cloudinary.uploader().upload(
                archivo.getBytes(),
                ObjectUtils.asMap(
                        "folder", "offroadparts4x4/repuestos",
                        "resource_type", "image"
                )
        );

        return resultado.get("secure_url").toString();
    }
}
