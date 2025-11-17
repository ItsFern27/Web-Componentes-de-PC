package com.pccore.pccore.controller.views;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pccore.pccore.model.Productos;
import com.pccore.pccore.repository.CategoriasRepository;
import com.pccore.pccore.repository.ProductosRepository;


@Controller
public class IndexController {

    private final ProductosRepository productosRepository;
    private final CategoriasRepository categoriasRepository;

    public IndexController(ProductosRepository productosRepository, CategoriasRepository categoriasRepository) {
        this.productosRepository = productosRepository;
        this.categoriasRepository = categoriasRepository;
    }

    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        List<Productos> productos = productosRepository.findAllWithItems();
        model.addAttribute("productos", productos);

        List<Productos> novedades = productosRepository.findTop3ByOrderByIdDesc();
        model.addAttribute("novedades", novedades);
        
        return "index";
    }

    @GetMapping("/contacto")
    public String getContacto() {
        return "contacto";
    }

    @GetMapping("/nosotros")
    public String getNosotros() {
        return "nosotros";
    }

    @GetMapping("/productos")
    public String getProductos(Model model, 
                               @RequestParam(name = "query", required = false) String query,
                               @RequestParam(name = "categoriaNombre", required = false) String categoriaNombre) {
        
        List<Productos> productos;
        boolean hasQuery = query != null && !query.isEmpty();
        boolean hasCategory = categoriaNombre != null && !categoriaNombre.isEmpty() && !categoriaNombre.equals("all");

        if (hasQuery && hasCategory) {
            productos = productosRepository.findByNombreContainingIgnoreCaseAndCategorias_Categoria_NombreContainingIgnoreCase(query, categoriaNombre);
        } else if (hasQuery) {
            productos = productosRepository.findByNombreContainingIgnoreCase(query);
        } else if (hasCategory) {
            productos = productosRepository.findByCategorias_Categoria_NombreContainingIgnoreCase(categoriaNombre);
        } else {
            productos = productosRepository.findAllWithItems();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("query", query);
        model.addAttribute("categorias", categoriasRepository.findAll());
        model.addAttribute("selectedCategoria", categoriaNombre);

        return "productos";
    }
    

}
