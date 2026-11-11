/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;

/**
 *
 * @author jurge
 */
import com.mycompany.proyecto.service.MovimientoInventarioService;
import com.mycompany.proyecto.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final MovimientoInventarioService movimientoInventarioService;
    private final ProductoService productoService;

    public InventarioController(
            MovimientoInventarioService movimientoInventarioService,
            ProductoService productoService) {

        this.movimientoInventarioService = movimientoInventarioService;
        this.productoService = productoService;
    }
    
@GetMapping
public String listar(Model model) {

    model.addAttribute("productos",
            productoService.listarTodos());

    return "inventario/listado";
}

@GetMapping("/historial")
public String historial(Model model) {

    model.addAttribute("movimientos",
            movimientoInventarioService.listarMovimientos());

    return "inventario/historial";
}

@PostMapping("/registrar")
public String registrar(
        @RequestParam Long productoId,
        @RequestParam String tipo,
        @RequestParam int cantidad,
        Principal principal,
        RedirectAttributes redirect) {

    try {
        movimientoInventarioService.registrarMovimiento(
                productoId,
                tipo,
                cantidad,
                principal.getName());

        redirect.addFlashAttribute("mensaje",
                "Movimiento registrado correctamente");

    } catch (Exception e) {

        redirect.addFlashAttribute("error",
                "No fue posible registrar el movimiento");
    }

    return "redirect:/inventario";
}
}
