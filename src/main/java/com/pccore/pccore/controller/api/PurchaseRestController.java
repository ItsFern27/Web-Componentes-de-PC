package com.pccore.pccore.controller.api;

import com.pccore.pccore.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PurchaseRestController {

    // Inyecta el servicio de compra para manejar la lógica de negocio
    private final PurchaseService purchaseService;

    /**
     * Endpoint para procesar la compra de un ítem.
     * Requiere autenticación del usuario.
     *
     * @param itemId El ID del ItemProducto a comprar.
     * @param principal Objeto Principal que representa al usuario autenticado.
     * @return ResponseEntity con el resultado de la operación de compra.
     */
    @PostMapping("/purchase/{itemId}")
    public ResponseEntity<String> purchaseItem(@PathVariable Long itemId, Principal principal) {
        // Verifica si el usuario está autenticado
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        // Obtiene el email del usuario autenticado (asumiendo que principal.getName() devuelve el email)
        String userEmail = principal.getName();

        // Procesa la compra a través del servicio
        boolean success = purchaseService.processPurchase(itemId, userEmail);

        // Retorna la respuesta basada en el resultado de la compra
        if (success) {
            return ResponseEntity.ok("Compra exitosa para el ítem ID: " + itemId);
        } else {
            // Si la compra falla, puede ser por ítem no disponible, no encontrado o algún otro error
            return ResponseEntity.badRequest().body("Fallo en la compra para el ítem ID: " + itemId + ". El ítem podría no estar disponible o ocurrió un error.");
        }
    }
}
