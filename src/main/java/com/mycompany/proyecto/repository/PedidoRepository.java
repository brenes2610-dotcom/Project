/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto.repository;
import com.mycompany.proyecto.domain.Pedido;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;
/**
 *
 * @author brene
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    
    Optional<Pedido> findByIdAndUsuarioCorreo(Long id, String correo);
    List<Pedido> findByUsuarioCorreoOrderByFechaDesc(String correo);
    
List<Pedido> findAllByOrderByFechaDesc();

List<Pedido> findByEstadoOrderByFechaDesc(String estado);

List<Pedido> findByFechaBetweenOrderByFechaDesc(
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin);

List<Pedido> findByEstadoAndFechaBetweenOrderByFechaDesc(
        String estado,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin);

}
