package com.pccore.pccore.controller.api.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pccore.pccore.service.DatabaseService;

@RestController
@RequestMapping("/admin")
public class DbQueryRestController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/query")
    public List<Map<String, Object>> mostrarConsulta() {
        return databaseService.obtenerConsulta();
    }

}
