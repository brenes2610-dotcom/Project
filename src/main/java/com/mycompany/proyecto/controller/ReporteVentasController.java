/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;

/**
 *
 * @author jurge
 */
import com.mycompany.proyecto.domain.Pedido;
import com.mycompany.proyecto.service.ReporteVentasService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/reportes/ventas")
public class ReporteVentasController {

    private final ReporteVentasService reporteVentasService;

    public ReporteVentasController(
            ReporteVentasService reporteVentasService) {

        this.reporteVentasService = reporteVentasService;
    }

    @GetMapping
    public String mostrarReporte(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            Model model) {

        LocalDateTime inicio = null;
        LocalDateTime fin = null;

        if (fechaInicio != null && !fechaInicio.isBlank()
                && fechaFin != null && !fechaFin.isBlank()) {

            inicio = LocalDate.parse(fechaInicio).atStartOfDay();

            fin = LocalDate.parse(fechaFin)
                    .plusDays(1).atStartOfDay().minusNanos(1);
        }

        List<Pedido> pedidos =
                reporteVentasService.consultarVentas(inicio, fin);

        model.addAttribute("totalVentas",
                reporteVentasService.calcularTotalVentas(pedidos));

        model.addAttribute("repuestosMasVendidos",
                reporteVentasService.calcularRepuestosMasVendidos(pedidos));

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "admin/reportes/ventas";
    }
}
