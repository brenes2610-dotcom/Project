/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;
import com.mycompany.proyecto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author brene
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
