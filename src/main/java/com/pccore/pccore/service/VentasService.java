package com.pccore.pccore.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.pccore.pccore.model.DetalleVenta;
import com.pccore.pccore.model.ItemProducto;
import com.pccore.pccore.model.Usuarios;
import com.pccore.pccore.model.Ventas;
import com.pccore.pccore.repository.DetalleVentaRepository;
import com.pccore.pccore.repository.ItemProductoRepository;
import com.pccore.pccore.repository.UsuariosRepository;
import com.pccore.pccore.repository.VentasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentasService {

    private final VentasRepository ventasRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ItemProductoRepository itemRepo;
    private final UsuariosRepository usuariosRepository;

    public void registrarVenta(Long itemId, String emailUsuario) {

        ItemProducto item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (item.getEstado().equals("Vendido")) {
            throw new RuntimeException("Este item ya fue vendido");
        }

        Usuarios usuario = usuariosRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no válido"));

        // Crear venta
        Ventas venta = new Ventas();
        venta.setCliente(usuario);
        venta.setFecha(OffsetDateTime.now());
        venta.setTotal(item.getPrecio());
        ventasRepository.save(venta);

        // Crear detalle de venta
        DetalleVenta det = new DetalleVenta();
        det.setVenta(venta);
        det.setItemProducto(item);
        det.setPrecio_venta(item.getPrecio());
        det.setDescuento(item.getPorcentaje_descuento());

        detalleVentaRepository.save(det);

        // Actualizar item como vendido
        item.setEstado("Vendido");
        itemRepo.save(item);
    }
}
