package com.pccore.pccore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    // Conexión al servicio de consultas a la BD
    private final JdbcTemplate jdbcTemplate;

    // Constructor del Service
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> obtenerConsulta() {
        return jdbcTemplate.queryForList("SELECT * FROM usuarios");
    }

    public Map<String, Object> obtenerConsultas() {
        Map<String, Object> resultado = new HashMap<>();

        resultado.put("tables", ejecutarConsulta("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"));
        resultado.put("usuarios", ejecutarConsulta("SELECT * FROM usuarios"));
        resultado.put("proyectos", ejecutarConsulta("SELECT * FROM proyectos"));

        return resultado;
    }
    
    private Object ejecutarConsulta(@NonNull String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}
