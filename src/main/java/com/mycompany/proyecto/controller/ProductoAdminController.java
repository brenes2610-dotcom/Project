/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;

import com.mycompany.proyecto.domain.Producto;
import com.mycompany.proyecto.domain.Categoria;
import com.mycompany.proyecto.domain.Marca;
import com.mycompany.proyecto.service.ProductoService;
import com.mycompany.proyecto.service.CategoriaService;
import com.mycompany.proyecto.service.MarcaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/productos")
public class ProductoAdminController {
    
private final ProductoService productoService;
private final CategoriaService categoriaService;
private final MarcaService marcaService;





public ProductoAdminController(
        ProductoService productoService,
        CategoriaService categoriaService,
        MarcaService marcaService) {

    this.productoService = productoService;
    this.categoriaService = categoriaService;
    this.marcaService = marcaService;
}



    @GetMapping
    public String listar(Model model) {

        model.addAttribute("productos",
                productoService.listarTodos());

        return "admin/productos/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        Producto producto = new Producto();
        producto.setCategoria(new Categoria());
        producto.setMarca(new Marca());

        model.addAttribute("producto", producto);
        model.addAttribute("categorias",
                categoriaService.listarActivas());
        model.addAttribute("marcas",
                marcaService.listar());

        return "admin/productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         RedirectAttributes redirect) {

        var producto = productoService.buscarPorId(id);
        
        if (producto.isEmpty()) {
            redirect.addFlashAttribute("error",
                    "El repuesto no existe");
            return "redirect:/admin/productos";
        }

        model.addAttribute("producto", producto.get());
        model.addAttribute("categorias",
                categoriaService.listarActivas());
        model.addAttribute("marcas",
                marcaService.listar());

        return "admin/productos/formulario";
    }

    

       @PostMapping("/guardar")
        public String guardar(
                        @ModelAttribute Producto producto,
                        RedirectAttributes redirect) {



    try {
        
        
        if (producto.getId() != null) {

            Producto existente = productoService
                    .buscarPorId(producto.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El repuesto no existe"));

            existente.setCodigo(producto.getCodigo());
            existente.setNombre(producto.getNombre());
            existente.setDescripcion(producto.getDescripcion());
            existente.setPrecio(producto.getPrecio());
            existente.setStock(producto.getStock());    
if (producto.getImagenUrl() != null
        && !producto.getImagenUrl().isBlank()) {

    existente.setImagenUrl(producto.getImagenUrl());
};
            existente.setCompatibilidad(producto.getCompatibilidad());
            existente.setCategoria(producto.getCategoria());
            existente.setMarca(producto.getMarca());

            productoService.guardar(existente);

        } else {

            producto.setActivo(true);
            productoService.guardar(producto);
        }

        redirect.addFlashAttribute("mensaje",
                "Repuesto guardado correctamente");

    }catch (Exception e) {
    e.printStackTrace();

    redirect.addFlashAttribute("error",
            "No fue posible guardar el repuesto");
}

    return "redirect:/admin/productos";
}


    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id,
                            RedirectAttributes redirect) {

        try {
            productoService.desactivar(id);

            redirect.addFlashAttribute("mensaje",
                    "Repuesto desactivado correctamente");

        } catch (Exception e) {
            redirect.addFlashAttribute("error",
                    "No fue posible desactivar el repuesto");
        }

        return "redirect:/admin/productos";
    }
    
@PostMapping("/activar/{id}")
public String activar(@PathVariable Long id,
                      RedirectAttributes redirect) {

    try {
        productoService.activar(id);

        redirect.addFlashAttribute("mensaje",
                "Repuesto activado correctamente");

    } catch (Exception e) {
        redirect.addFlashAttribute("error",
                "No fue posible activar el repuesto");
    }

    return "redirect:/admin/productos";
}

}
