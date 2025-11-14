package com.pccore.pccore.service;

import com.pccore.pccore.model.Usuarios;
import com.pccore.pccore.repository.UsuariosRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;
    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Autenticando usuario: {}", email);
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        log.info("Usuario encontrado: {} (rol={})", usuario.getEmail(), usuario.getRol());

        // Sin password hasheado
        return User.withUsername(usuario.getEmail())
            .password(usuario.getPassword())
            .roles(usuario.getRol())
            .build();
    }
}
