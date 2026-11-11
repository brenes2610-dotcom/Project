/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;

/**
 *
 * @author jurge
 */

import com.mycompany.proyecto.domain.MovimientoInventario;
import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.domain.Usuario;

import com.mycompany.proyecto.repository.MovimientoInventarioRepository;
import com.mycompany.proyecto.repository.ProductoRepository;
import com.mycompany.proyecto.repository.UsuarioRepository;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoInventarioService(
            MovimientoInventarioRepository movimientoRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {

        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
@Transactional
public void registrarMovimiento(Long productoId,
        String tipo, int cantidad, String correo) {

    Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new IllegalArgumentException(
                    "El repuesto no existe"));

    Usuario usuario = usuarioRepository.findByCorreo(correo);

    if (usuario == null) {
        throw new IllegalArgumentException(
                "El usuario responsable no existe");
    }

    int stockAnterior = producto.getStock();
    int stockNuevo;

    if ("ENTRADA".equals(tipo)) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        stockNuevo = stockAnterior + cantidad;

    } else if ("AJUSTE".equals(tipo)) {

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo");
        }

        stockNuevo = cantidad;

    } else {
        throw new IllegalArgumentException(
                "Tipo de movimiento no válido");
    }

    producto.setStock(stockNuevo);
    productoRepository.save(producto);

    MovimientoInventario movimiento = new MovimientoInventario();

    movimiento.setProducto(producto);
    movimiento.setUsuario(usuario);
    movimiento.setTipo(tipo);
    movimiento.setCantidad(cantidad);
    movimiento.setStockAnterior(stockAnterior);
    movimiento.setStockNuevo(stockNuevo);
    movimiento.setFecha(LocalDateTime.now());

    movimientoRepository.save(movimiento);
}

     @Transactional(readOnly = true)
    public List<MovimientoInventario> listarMovimientos() {
    return movimientoRepository.findAllByOrderByFechaDesc();
}

}
