package com.pccore.pccore.config;

import com.pccore.pccore.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /* Coso para evitar que otros sitios hagan POST a nuestras API con la sesión del usuario, cuando esta habilitado esto genera un token que se pone en los forms */
            .csrf(AbstractHttpConfigurer::disable) // coso generico de spring security que desactiva el csrf

            .authorizeHttpRequests(auth -> auth
                .requestMatchers( /* URLS habilitadas para visitar sin login */
                    "/login", "/acceso-denegado", "/auth/login", "/auth/register", "/auth/me", "/", "/index",
                    "/productos", "/nosotros", "/contacto",
                    "/styles/**", "/imagenes/**", "/js/**", "/css/**", "/favicon.ico", "/webjars/**", "/debug/**", "/public-debug/**", "/public/**"
                ).permitAll()

                .requestMatchers("/admin/**").hasRole("ADMIN") /* URLS habilitadas con rol especifico */

                .anyRequest().authenticated() /* Cualquier request se necesita estar logueado, exceptuando las habilitadas */
            )

            /* Configuracion del sistema de login por formulario de HTML normal */
            /* Esto se puede desactivar y manejarlo manualmente por RestController pero Sí */
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll() /* URLS habilitadas para visitar sin login */
            )

            /* Manejo del logout automatico, se puede hacer manual por RestControllers */
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )

            /* Manejar que hacer cuando un usuario intenta acceder a algo que no tiene permisos */
            /* EJM: Usuario CLIENTE entra a /admin */
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // Usuario no autenticado intenta entrar a recurso protegido -> llevar a login
                    response.sendRedirect("/");
                })
                .accessDeniedPage("/login?denied")
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());


        return authenticationManagerBuilder.build();
    }
}
