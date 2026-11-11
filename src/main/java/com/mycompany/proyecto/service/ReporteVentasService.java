/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;

/**
 *
 * @author jurge
 */
import com.mycompany.proyecto.domain.Pedido;
import com.mycompany.proyecto.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import com.mycompany.proyecto.domain.DetallePedido;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
public class ReporteVentasService {

    private final PedidoRepository pedidoRepository;

    public ReporteVentasService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> consultarVentas(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return pedidoRepository
                    .findByFechaBetweenOrderByFechaDesc(
                            fechaInicio, fechaFin);
        }

        return pedidoRepository.findAllByOrderByFechaDesc();
    }
    
public BigDecimal calcularTotalVentas(List<Pedido> pedidos) {

    BigDecimal total = BigDecimal.ZERO;

    for (Pedido pedido : pedidos) {

        if (!"CANCELADO".equals(pedido.getEstado())) {

            total = total.add(pedido.getTotal());

        }
    }

    return total;
}

public Map<String, Integer> calcularRepuestosMasVendidos(
        List<Pedido> pedidos) {

    Map<String, Integer> repuestos = new HashMap<>();

    for (Pedido pedido : pedidos) {

        if (!"CANCELADO".equals(pedido.getEstado())) {

            for (DetallePedido detalle : pedido.getDetalles()) {

                String nombre = detalle.getProducto().getNombre();
                int cantidad = detalle.getCantidad();

                int acumulado = repuestos.getOrDefault(nombre, 0);

                repuestos.put(nombre, acumulado + cantidad);
            }
        }
    }

   
return repuestos.entrySet()
        .stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (anterior, nuevo) -> anterior,
                LinkedHashMap::new
        ));

}

}
