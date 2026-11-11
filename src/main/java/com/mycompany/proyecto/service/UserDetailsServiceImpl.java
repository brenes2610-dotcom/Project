/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.service;
import com.mycompany.proyecto.domain.Usuario;
import com.mycompany.proyecto.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 *
 * @author brene
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UsuarioRepository usuarioRepository;

        public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
            this.usuarioRepository = usuarioRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
            Usuario u = usuarioRepository.findByCorreo(correo);
            if (u == null) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
            return User.withUsername(u.getCorreo())
                    .password(u.getPassword())
                    .roles(u.getRol().getNombre())
                    .disabled(!u.isActivo())
                    .build();
    }
}
