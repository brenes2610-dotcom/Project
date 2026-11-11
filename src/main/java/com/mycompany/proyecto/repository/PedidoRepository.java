/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;
import com.mycompany.proyecto.domain.Pedido;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author brene
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
    Optional<Pedido> findByIdAndUsuarioCorreo(Long id, String correo);
}
