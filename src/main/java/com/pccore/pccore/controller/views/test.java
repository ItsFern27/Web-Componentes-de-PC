package com.pccore.pccore.controller.views;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @GetMapping("/yo")
    public Map<String, Object> perfilUsuario(@AuthenticationPrincipal UserDetails user, Model model) {

        Map<String, Object> res = new HashMap<>();
        res.put("user", user.getUsername());
        res.put("es", user.getPassword());
        res.put("ds", user.getAuthorities());

        return res;
    }
}
