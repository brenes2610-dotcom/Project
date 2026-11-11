/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;

import com.mycompany.proyecto.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author brene
 */
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    Optional<Producto> findByIdAndActivoTrue(Long id);

    @Query("SELECT p FROM Producto p WHERE p.activo = true "
            + "AND (:idCategoria IS NULL OR p.categoria.id = :idCategoria) "
            + "AND (:idMarca IS NULL OR p.marca.id = :idMarca) "
            + "AND (:precioMin IS NULL OR p.precio >= :precioMin) "
            + "AND (:precioMax IS NULL OR p.precio <= :precioMax) "
            + "ORDER BY p.nombre ASC")
    List<Producto> filtrar(@Param("idCategoria") Long idCategoria,
                           @Param("idMarca") Long idMarca,
                           @Param("precioMin") BigDecimal precioMin,
                           @Param("precioMax") BigDecimal precioMax);
}
