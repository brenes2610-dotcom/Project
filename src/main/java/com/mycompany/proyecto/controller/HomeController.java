/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author brene
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/panel";
    }
}
