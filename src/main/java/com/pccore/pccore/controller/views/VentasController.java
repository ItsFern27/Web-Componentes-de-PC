package com.pccore.pccore.controller.views;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pccore.pccore.service.VentasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentasController {

    private final VentasService ventasService;

    @PostMapping("/comprar/{itemId}")
    public ResponseEntity<String> comprarItem(@PathVariable Long itemId, Principal principal) {
        try {
            ventasService.registrarVenta(itemId, principal.getName());
            return ResponseEntity.ok("Venta realizada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

}


