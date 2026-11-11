/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;
import com.mycompany.proyecto.domain.Carrito;
import com.mycompany.proyecto.domain.Pedido;
import com.mycompany.proyecto.service.CarritoService;
import com.mycompany.proyecto.service.PedidoService;
import java.security.Principal;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author brene
 */
@Controller
@RequestMapping("/compra")
public class CompraController {
    
    private final PedidoService pedidoService;
    private final CarritoService carritoService;

    public CompraController(PedidoService pedidoService, CarritoService carritoService) {
        this.pedidoService = pedidoService;
        this.carritoService = carritoService;
    }

    @GetMapping("/confirmar")
    public String formulario(Principal principal, Model model,
                             RedirectAttributes redirectAttributes) {
        Carrito carrito = carritoService.obtenerCarrito(principal.getName());
        if (carrito.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío");
            return "redirect:/carrito";
        }
        model.addAttribute("carrito", carrito);
        model.addAttribute("metodosPago", PedidoService.METODOS_PAGO);
        return "compra/confirmar";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam String direccionEntrega,
                            @RequestParam String metodoPago,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(
                    principal.getName(), direccionEntrega, metodoPago);
            redirectAttributes.addFlashAttribute("todoOk", "¡Tu pedido fue registrado con éxito!");
            return "redirect:/compra/resumen/" + pedido.getId();
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/compra/confirmar";
        }
    }

    @GetMapping("/resumen/{id}")
    public String resumen(@PathVariable Long id, Principal principal, Model model,
                          RedirectAttributes redirectAttributes) {
        Optional<Pedido> pedido = pedidoService.buscarDeUsuario(id, principal.getName());
        if (pedido.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/carrito";
        }
        model.addAttribute("pedido", pedido.get());
        return "compra/resumen";
    }
}
