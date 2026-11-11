/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;
import com.mycompany.proyecto.service.CarritoService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author brene
 */
@Controller
@RequestMapping("/carrito")
public class CarritoController {
     private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public String ver(Principal principal, Model model) {
        model.addAttribute("carrito", carritoService.obtenerCarrito(principal.getName()));
        return "carrito/ver";
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Long idProducto,
                          @RequestParam(defaultValue = "1") int cantidad,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {
        try {
            carritoService.agregar(principal.getName(), idProducto, cantidad);
            redirectAttributes.addFlashAttribute("todoOk", "Repuesto agregado al carrito");
            return "redirect:/carrito";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/catalogo/repuesto/" + idProducto;
        }
    }

    @PostMapping("/cantidad")
    public String cambiarCantidad(@RequestParam Long idItem,
                                  @RequestParam int cantidad,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        try {
            carritoService.cambiarCantidad(principal.getName(), idItem, cantidad);
            redirectAttributes.addFlashAttribute("todoOk", "Carrito actualizado");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/quitar")
    public String quitar(@RequestParam Long idItem,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            carritoService.quitar(principal.getName(), idItem);
            redirectAttributes.addFlashAttribute("todoOk", "Repuesto eliminado del carrito");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }
}
