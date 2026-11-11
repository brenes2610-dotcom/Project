/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;

/**
 *
 * @author jurge
 */

import com.mycompany.proyecto.service.PedidoService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoAdminController {

    private final PedidoService pedidoService;

    public PedidoAdminController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String estado,
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

        model.addAttribute("pedidos",
                pedidoService.listarPedidos(estado, inicio, fin));

        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "admin/pedidos/listado";
    }
    
@PostMapping("/estado/{id}")
public String cambiarEstado(
        @PathVariable Long id,
        @RequestParam String estado,
        RedirectAttributes redirect) {

    try {
        pedidoService.cambiarEstado(id, estado);

        redirect.addFlashAttribute("mensaje",
                "Estado del pedido actualizado correctamente");

    } catch (Exception e) {

        redirect.addFlashAttribute("error",
                "No fue posible actualizar el estado del pedido");
    }

    return "redirect:/admin/pedidos";
}

}
