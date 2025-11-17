package com.pccore.pccore.controller.views.admin;

import com.pccore.pccore.model.ItemProducto;
import com.pccore.pccore.model.Productos;
import com.pccore.pccore.repository.CategoriasRepository;
import com.pccore.pccore.repository.ItemProductoRepository;
import com.pccore.pccore.repository.ProductosRepository;
import com.pccore.pccore.repository.ProveedoresRepository;
import com.pccore.pccore.specification.ItemProductoSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductosRepository productosRepository;
    private final ItemProductoRepository itemProductoRepository;
    private final CategoriasRepository categoriasRepository;
    private final ProveedoresRepository proveedoresRepository;

    public AdminController(ProductosRepository productosRepository,
                           ItemProductoRepository itemProductoRepository,
                           CategoriasRepository categoriasRepository,
                           ProveedoresRepository proveedoresRepository) {
        this.productosRepository = productosRepository;
        this.itemProductoRepository = itemProductoRepository;
        this.categoriasRepository = categoriasRepository;
        this.proveedoresRepository = proveedoresRepository;
    }

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

    @PostMapping("/items/eliminar/{id}")
    public String eliminarItem(@PathVariable Long id) {
        itemProductoRepository.deleteById(id);
        return "redirect:/admin/busqueda_avanzada";
    }

    @GetMapping("/items/editar/{id}")
    public String mostrarFormularioEditarItem(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemProductoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id)));
        model.addAttribute("productos", productosRepository.findAll());
        model.addAttribute("proveedores", proveedoresRepository.findAll());
        return "admin/editar_item";
    }

    @PostMapping("/items/editar/{id}")
    public String editarItem(@PathVariable Long id, @ModelAttribute ItemProducto itemForm) {
        ItemProducto existingItem = itemProductoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));

        // Update fields from form, preserving existing fecha_ingreso if not provided in form
        existingItem.setProducto(itemForm.getProducto());
        existingItem.setProveedor(itemForm.getProveedor());
        existingItem.setPrecio(itemForm.getPrecio());
        existingItem.setNumero_de_serie(itemForm.getNumero_de_serie());
        existingItem.setEstado(itemForm.getEstado());
        existingItem.setPorcentaje_descuento(itemForm.getPorcentaje_descuento());
        
        // Only update fecha_ingreso if it was provided in the form
        if (itemForm.getFecha_ingreso() != null) {
            existingItem.setFecha_ingreso(itemForm.getFecha_ingreso());
        }

        itemProductoRepository.save(existingItem);
        return "redirect:/admin/busqueda_avanzada";
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

    @GetMapping("/busqueda_avanzada")
    public String busquedaAvanzada(
            @RequestParam(required = false) String productoNombre,
            @RequestParam(required = false) String productoMarca,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Boolean disponibleFilter,
            Model model) {

        Specification<ItemProducto> spec = ItemProductoSpecification.findByCriteria(
                productoNombre, productoMarca, proveedorId, categoriaId, fechaDesde, fechaHasta, disponibleFilter);
        
        List<ItemProducto> items = itemProductoRepository.findAll(spec);

        model.addAttribute("items", items);
        model.addAttribute("categorias", categoriasRepository.findAll());
        model.addAttribute("proveedores", proveedoresRepository.findAll());

        // Pass back search params to pre-fill the form
        model.addAttribute("productoNombre", productoNombre);
        model.addAttribute("productoMarca", productoMarca);
        model.addAttribute("proveedorId", proveedorId);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);
        model.addAttribute("disponibleFilter", disponibleFilter);

        return "admin/busqueda_avanzada";
    }
}
