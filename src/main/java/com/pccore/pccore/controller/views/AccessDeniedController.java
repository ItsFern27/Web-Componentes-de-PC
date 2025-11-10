package com.pccore.pccore.controller.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    @GetMapping("/views/acceso-denegado")
    public String accesoDenegado() {
        return "views/acceso-denegado";
    }
}
