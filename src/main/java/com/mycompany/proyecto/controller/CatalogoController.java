/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;
import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.service.CategoriaService;
import com.mycompany.proyecto.service.MarcaService;
import com.mycompany.proyecto.service.ProductoService;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author brene
 */
@Controller
@RequestMapping("/catalogo")
public class CatalogoController {
      private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MarcaService marcaService;

    public CatalogoController(ProductoService productoService,
                              CategoriaService categoriaService,
                              MarcaService marcaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.marcaService = marcaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long categoria,
                         @RequestParam(required = false) Long marca,
                         @RequestParam(required = false) BigDecimal precioMin,
                         @RequestParam(required = false) BigDecimal precioMax,
                         Model model) {
        model.addAttribute("repuestos",
                productoService.filtrar(categoria, marca, precioMin, precioMax));
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("marcas", marcaService.listar());
        model.addAttribute("categoriaSel", categoria);
        model.addAttribute("marcaSel", marca);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        return "catalogo/listado";
    }

    @GetMapping("/repuesto/{id}")
    public String detalle(@PathVariable Long id, Model model,
                          RedirectAttributes redirectAttributes) {
        Optional<Producto> repuesto = productoService.buscarActivo(id);
        if (repuesto.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El repuesto solicitado no existe");
            return "redirect:/catalogo";
        }
        model.addAttribute("repuesto", repuesto.get());
        return "catalogo/detalle";
    }
}
