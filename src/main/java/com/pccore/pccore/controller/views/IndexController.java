package com.pccore.pccore.controller.views;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pccore.pccore.model.Productos;
import com.pccore.pccore.repository.ProductosRepository;


@Controller
public class IndexController {

    private final ProductosRepository productosRepository;

    public IndexController(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
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
    public String getProductos(Model model) {
        List<Productos> productos = productosRepository.findAllWithItems();
        model.addAttribute("productos", productos);
        return "productos";
    }
    

}
