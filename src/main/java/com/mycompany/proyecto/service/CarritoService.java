/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;
import com.mycompany.proyecto.domain.Carrito;
import com.mycompany.proyecto.domain.CarritoItem;
import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.domain.Usuario;
import com.mycompany.proyecto.repository.CarritoRepository;
import com.mycompany.proyecto.repository.ProductoRepository;
import com.mycompany.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author brene
 */
@Service
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          UsuarioRepository usuarioRepository,
                          ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Carrito obtenerCarrito(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return carritoRepository.findByUsuarioId(usuario.getId()).orElseGet(() -> {
            Carrito nuevo = new Carrito();
            nuevo.setUsuario(usuario);
            return carritoRepository.save(nuevo);
        });
    }

    @Transactional
    public void agregar(String correo, Long idProducto, int cantidad) {
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }
        Producto producto = productoRepository.findByIdAndActivoTrue(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El repuesto no existe"));
        if (producto.getStock() == 0) {
            throw new IllegalStateException("El repuesto está agotado");
        }

        Carrito carrito = obtenerCarrito(correo);
        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(idProducto))
                .findFirst()
                .orElse(null);

        int nuevaCantidad = cantidad + (item == null ? 0 : item.getCantidad());
        validarStock(producto, nuevaCantidad);

        if (item == null) {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            carrito.getItems().add(item);
        }
        item.setCantidad(nuevaCantidad);
        carritoRepository.save(carrito);
    }

    @Transactional
    public void cambiarCantidad(String correo, Long idItem, int cantidad) {
        Carrito carrito = obtenerCarrito(correo);
        CarritoItem item = buscarItem(carrito, idItem);
        if (cantidad < 1) {
            carrito.getItems().remove(item);
        } else {
            validarStock(item.getProducto(), cantidad);
            item.setCantidad(cantidad);
        }
        carritoRepository.save(carrito);
    }

    @Transactional
    public void quitar(String correo, Long idItem) {
        Carrito carrito = obtenerCarrito(correo);
        CarritoItem item = buscarItem(carrito, idItem);
        carrito.getItems().remove(item);
        carritoRepository.save(carrito);
    }

    private CarritoItem buscarItem(Carrito carrito, Long idItem) {
        return carrito.getItems().stream()
                .filter(i -> i.getId().equals(idItem))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "El artículo no está en tu carrito"));
    }

    private void validarStock(Producto producto, int cantidad) {
        if (cantidad > producto.getStock()) {
            throw new IllegalStateException(
                    "La cantidad supera el stock disponible (" + producto.getStock() + ")");
        }
    }
}
