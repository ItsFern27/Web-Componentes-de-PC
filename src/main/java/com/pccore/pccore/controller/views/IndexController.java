package com.pccore.pccore.controller.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pccore.pccore.service.DatabaseService;


@Controller
public class IndexController {

    private final DatabaseService databaseService;

    public IndexController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("usuarios", databaseService.obtenerConsulta());
        return "index";
    }
    

}
