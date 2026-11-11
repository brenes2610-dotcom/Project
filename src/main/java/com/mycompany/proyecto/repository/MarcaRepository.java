/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;
import com.mycompany.proyecto.domain.Marca;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author brene
 */
public interface MarcaRepository extends JpaRepository<Marca, Long>{
     List<Marca> findAllByOrderByNombreAsc();
}
