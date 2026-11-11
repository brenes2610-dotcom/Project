/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;
import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author brene
 */
@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> filtrar(Long idCategoria, Long idMarca,
                                  BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin != null && precioMax != null && precioMin.compareTo(precioMax) > 0) {
            BigDecimal temporal = precioMin;
            precioMin = precioMax;
            precioMax = temporal;
        }
        return productoRepository.filtrar(idCategoria, idMarca, precioMin, precioMax);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarActivo(Long id) {
        return productoRepository.findByIdAndActivoTrue(id);
    }
}
