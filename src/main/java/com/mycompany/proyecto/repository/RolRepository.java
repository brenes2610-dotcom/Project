/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;
import com.mycompany.proyecto.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author brene
 */
public interface RolRepository  extends JpaRepository<Rol, Long>  {
    Rol findByNombre(String nombre);
}
