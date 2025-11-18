package com.pccore.pccore.controller.views.admin;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pccore.pccore.model.ItemProducto;
import com.pccore.pccore.model.Productos;
import com.pccore.pccore.model.Proveedores;
import com.pccore.pccore.repository.CategoriasRepository;
import com.pccore.pccore.repository.ItemProductoRepository;
import com.pccore.pccore.repository.ProductosRepository;
import com.pccore.pccore.repository.ProveedoresRepository;
import com.pccore.pccore.specification.ItemProductoSpecification;


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
    public String crearProducto(@ModelAttribute("productoNuevo") Productos producto, RedirectAttributes redirectAttributes) {
        try {
            productosRepository.save(producto);
            redirectAttributes.addFlashAttribute("successMessage", "Producto creado exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el producto. El 'modelo' ya existe.");
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, @ModelAttribute Productos producto, RedirectAttributes redirectAttributes) {
        producto.setId(id);
        try {
            productosRepository.save(producto);
            redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al editar el producto. El 'modelo' ya existe.");
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Check if there are related items
        if (itemProductoRepository.existsByProductoId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar el producto porque tiene items asociados.");
            return "redirect:/admin/productos";
        }
        try {
            productosRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producto eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el producto.");
        }
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
    public String gestionarCategorias(Model model) {
        model.addAttribute("categorias", categoriasRepository.findAll());
        model.addAttribute("categoriaNueva", new com.pccore.pccore.model.Categorias());
        return "admin/gestionar_categorias";
    }

    @PostMapping("/categorias/crear")
    public String crearCategoria(@ModelAttribute("categoriaNueva") com.pccore.pccore.model.Categorias categoria, RedirectAttributes redirectAttributes) {
        try {
            categoriasRepository.save(categoria);
            redirectAttributes.addFlashAttribute("successMessage", "Categoría creada exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear la categoría.");
        }
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/editar/{id}")
    public String editarCategoria(@PathVariable Long id, @ModelAttribute com.pccore.pccore.model.Categorias categoria, RedirectAttributes redirectAttributes) {
        categoria.setId(id);
        try {
            categoriasRepository.save(categoria);
            redirectAttributes.addFlashAttribute("successMessage", "Categoría actualizada exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al editar la categoría.");
        }
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriasRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Categoría eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la categoría.");
        }
        return "redirect:/admin/categorias";
    }

    @GetMapping("/items")
    public String gestionarItems(Model model) {
        model.addAttribute("itemNuevo", new ItemProducto());
        model.addAttribute("productos", productosRepository.findAll());
        model.addAttribute("proveedores", proveedoresRepository.findAll());
        return "admin/gestionar_items";
    }

    @PostMapping("/items/crear")
    public String crearItem(@ModelAttribute("itemNuevo") ItemProducto item, RedirectAttributes redirectAttributes) {
        try {
            item.setFecha_ingreso(OffsetDateTime.now());
            item.setEstado("DISPONIBLE");
            itemProductoRepository.save(item);
            redirectAttributes.addFlashAttribute("successMessage", "Item creado exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el item.");
        }
        return "redirect:/admin/items";
    }

    @GetMapping("/proveedores")
    public String gestionarProveedores(Model model) {
        model.addAttribute("proveedores", proveedoresRepository.findAll());
        model.addAttribute("proveedorNuevo", new com.pccore.pccore.model.Proveedores());
        return "admin/gestionar_proveedores";
    }

    @PostMapping("/proveedores/crear")
    public String crearProveedor(@ModelAttribute("proveedorNuevo") Proveedores proveedor,
                                RedirectAttributes redirectAttributes) {
        try {
            proveedoresRepository.save(proveedor);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor creado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el proveedor.");
        }
        return "redirect:/admin/proveedores";
    }

    @PostMapping("/proveedores/editar/{id}")
    public String editarProveedor(@PathVariable Long id,
                                @ModelAttribute Proveedores proveedor,
                                RedirectAttributes redirectAttributes) {

        proveedor.setId(id);

        try {
            proveedoresRepository.save(proveedor);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al editar el proveedor.");
        }

        return "redirect:/admin/proveedores";
    }

    @PostMapping("/proveedores/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            proveedoresRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar el proveedor. Es posible que esté asociado a items.");
        }

        return "redirect:/admin/proveedores";
    }

    @GetMapping("/busqueda_avanzada")
    public String busquedaAvanzada(
            @RequestParam(required = false) String productoNombre,
            @RequestParam(required = false) String productoMarca,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required =false) Boolean disponibleFilter,
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
