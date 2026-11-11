/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;
import com.mycompany.proyecto.domain.Carrito;
import com.mycompany.proyecto.domain.CarritoItem;
import com.mycompany.proyecto.domain.DetallePedido;
import com.mycompany.proyecto.domain.Pedido;
import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.repository.CarritoRepository;
import com.mycompany.proyecto.repository.PedidoRepository;
import com.mycompany.proyecto.repository.ProductoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author brene
 */

@Service
public class PedidoService {
    public static final List<String> METODOS_PAGO =
            List.of("SINPE Móvil", "Tarjeta", "Contra entrega");

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoService carritoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoRepository productoRepository,
                         CarritoRepository carritoRepository,
                         CarritoService carritoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.carritoRepository = carritoRepository;
        this.carritoService = carritoService;
    }

    @Transactional
    public Pedido confirmarPedido(String correo, String direccionEntrega, String metodoPago) {
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La dirección de entrega es obligatoria");
        }
        if (metodoPago == null || !METODOS_PAGO.contains(metodoPago)) {
            throw new IllegalArgumentException("Seleccioná un método de pago válido");
        }

        Carrito carrito = carritoService.obtenerCarrito(correo);
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("Tu carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setDireccionEntrega(direccionEntrega.trim());
        pedido.setMetodoPago(metodoPago);

        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new IllegalStateException("Un repuesto ya no existe"));

            if (!producto.isActivo() || item.getCantidad() > producto.getStock()) {
                throw new IllegalStateException("No hay stock suficiente de "
                        + producto.getNombre() + " (disponible: " + producto.getStock() + ")");
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            pedido.getDetalles().add(detalle);

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            total = total.add(detalle.getSubtotal());
        }
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return guardado;
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarDeUsuario(Long idPedido, String correo) {
        return pedidoRepository.findByIdAndUsuarioCorreo(idPedido, correo);
    }
}
