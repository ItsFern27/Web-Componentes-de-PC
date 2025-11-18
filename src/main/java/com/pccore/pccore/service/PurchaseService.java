package com.pccore.pccore.service;

import com.pccore.pccore.model.DetalleVenta;
import com.pccore.pccore.model.ItemProducto;
import com.pccore.pccore.model.Usuarios;
import com.pccore.pccore.model.Ventas;
import com.pccore.pccore.repository.DetalleVentaRepository;
import com.pccore.pccore.repository.ItemProductoRepository;
import com.pccore.pccore.repository.UsuariosRepository;
import com.pccore.pccore.repository.VentasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    // Repositorios necesarios para interactuar con la base de datos
    private final ItemProductoRepository itemProductoRepository;
    private final VentasRepository ventasRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final UsuariosRepository usuariosRepository;

    /**
     * Procesa la compra de un ítem específico por un usuario.
     *
     * @param itemId El ID del ItemProducto a comprar.
     * @param userEmail El email del usuario que realiza la compra.
     * @return true si la compra fue exitosa, false en caso contrario.
     */
    @Transactional // Asegura que toda la operación sea atómica
    public boolean processPurchase(Long itemId, String userEmail) {
        // Busca el ítem del producto por su ID
        Optional<ItemProducto> optionalItem = itemProductoRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            // Si el ítem no se encuentra, la compra falla
            return false;
        }

        ItemProducto item = optionalItem.get();

        // Verifica si el ítem está disponible para la venta
        if (!"DISPONIBLE".equals(item.getEstado())) {
            // Si el ítem no está disponible, la compra falla
            return false;
        }

        // Busca al usuario por su email
        Optional<Usuarios> optionalUser = usuariosRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            // Si el usuario no se encuentra (lo cual no debería ocurrir si está autenticado), la compra falla
            return false;
        }
        Usuarios user = optionalUser.get();

        // Crea una nueva venta. Por simplicidad, cada compra crea una nueva venta.
        // En una aplicación real, se podría manejar un "carrito de compras" para agrupar ítems.
        Ventas venta = new Ventas();
        venta.setFecha(OffsetDateTime.now()); // Establece la fecha actual de la venta
        venta.setCliente(user); // Asigna el cliente a la venta
        
        // Calcula el total de la venta considerando el descuento del ítem
        BigDecimal precioConDescuento = item.getPrecio().subtract(
            item.getPrecio().multiply(BigDecimal.valueOf(item.getPorcentajeDescuento()))
            .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP) // Redondeo para precisión
        );
        venta.setTotal(precioConDescuento);
        venta = ventasRepository.save(venta); // Guarda la venta para obtener su ID

        // Crea el detalle de la venta para el ítem comprado
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setVenta(venta); // Asigna la venta al detalle
        detalleVenta.setItemProducto(item); // Asigna el ítem al detalle
        detalleVenta.setPrecioVenta(item.getPrecio()); // Precio original del ítem
        
        // Calcula el monto del descuento
        BigDecimal montoDescuento = item.getPrecio().multiply(BigDecimal.valueOf(item.getPorcentajeDescuento()))
                                    .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);
        detalleVenta.setDescuento(montoDescuento);
        
        // Calcula el subtotal (precio de venta - descuento)
        detalleVenta.setSubtotal(item.getPrecio().subtract(montoDescuento));
        detalleVentaRepository.save(detalleVenta); // Guarda el detalle de la venta

        // Actualiza el estado del ítem a "VENDIDO"
        item.setEstado("VENDIDO");
        itemProductoRepository.save(item); // Guarda el ítem con el nuevo estado

        return true; // La compra fue exitosa
    }
}