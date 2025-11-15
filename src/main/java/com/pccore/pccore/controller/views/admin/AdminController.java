package com.pccore.pccore.controller.views.admin;

import com.pccore.pccore.model.Productos;
import com.pccore.pccore.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductosRepository productosRepository;

    @GetMapping
    public String adminHome() {
        return "admin/admin_dashboard";
    }

    @GetMapping("/productos")
    public String gestionarProductos(Model model) {
        model.addAttribute("productos", productosRepository.findAll());
        model.addAttribute("productoNuevo", new Productos());
        return "admin/gestionar_productos";
    }

    @PostMapping("/productos/crear")
    public String crearProducto(@ModelAttribute("productoNuevo") Productos producto) {
        productosRepository.save(producto);
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, @ModelAttribute Productos producto) {
        producto.setId(id);
        productosRepository.save(producto);
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productosRepository.deleteById(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/categorias")
    public String gestionarCategorias() {
        return "admin/gestionar_categorias";
    }

    @GetMapping("/items")
    public String gestionarItems() {
        return "admin/gestionar_items";
    }

    @GetMapping("/proveedores")
    public String gestionarProveedores() {
        return "admin/gestionar_proveedores";
    }

    @GetMapping("/busqueda")
    public String busquedaAvanzada() {
        return "admin/busqueda_avanzada";
    }
}
