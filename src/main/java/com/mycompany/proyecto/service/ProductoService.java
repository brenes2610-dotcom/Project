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
    
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    
@Transactional
public Producto guardar(Producto producto) {

    if (producto.getCodigo() == null
            || producto.getCodigo().isBlank()) {
        throw new IllegalArgumentException(
                "El código del repuesto es obligatorio");
    }

    if (producto.getNombre() == null
            || producto.getNombre().isBlank()) {
        throw new IllegalArgumentException(
                "El nombre del repuesto es obligatorio");
    }

    if (producto.getPrecio() == null
            || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException(
                "El precio debe ser mayor que cero");
    }

    if (producto.getStock() < 0) {
        throw new IllegalArgumentException(
                "El stock no puede ser negativo");
    }

    if (producto.getCategoria() == null
            || producto.getCategoria().getId() == null) {
        throw new IllegalArgumentException(
                "Debe seleccionar una categoría");
    }

    if (producto.getMarca() == null
            || producto.getMarca().getId() == null) {
        throw new IllegalArgumentException(
                "Debe seleccionar una marca");
    }

    return productoRepository.save(producto);
}

    @Transactional
    public void desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El repuesto no existe"));

        producto.setActivo(false);
        productoRepository.save(producto);
    }

@Transactional
public void activar(Long id) {

    Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                    "El repuesto no existe"));

    producto.setActivo(true);
    productoRepository.save(producto);
}

}
