/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;
import com.mycompany.proyecto.domain.Rol;
import com.mycompany.proyecto.domain.Usuario;
import com.mycompany.proyecto.repository.RolRepository;
import com.mycompany.proyecto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author brene
 */
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
        public boolean correoExiste(String correo) {
            return usuarioRepository.existsByCorreo(correo);
        }

    @Transactional
        public Usuario registrarCliente(Usuario usuario) {
            Rol rolCliente = rolRepository.findByNombre("CLIENTE");
            if (rolCliente == null) {
                throw new IllegalStateException("No existe el rol CLIENTE en la base de datos");
            }
            usuario.setRol(rolCliente);
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
    }
}
