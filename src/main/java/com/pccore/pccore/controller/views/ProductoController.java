package com.pccore.pccore.controller.views;

import com.pccore.pccore.model.Productos;
import com.pccore.pccore.repository.ProductosRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProductoController {

    private final ProductosRepository productosRepository;

    public ProductoController(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @GetMapping("/producto/{id}")
    public String getProducto(@PathVariable Long id, Model model) {
        Productos producto = productosRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("producto", producto);
        return "producto";
    }

}
