/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;

import com.mycompany.proyecto.service.PedidoService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class HistorialdepedidoController {

    private final PedidoService pedidoService;

    public HistorialdepedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/historial")
    public String mostrarHistorial(Principal principal, Model model) {

        model.addAttribute("pedidos",
                pedidoService.consultarHistorial(principal.getName()));

        return "pedidos/historial";
    }
}
