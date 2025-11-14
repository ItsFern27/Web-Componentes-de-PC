package com.pccore.pccore.controller;

import com.pccore.pccore.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DebugController {

    private final UsuariosRepository usuariosRepository;

    @GetMapping("/debug/userExists")
    public String userExists(@RequestParam String email) {
        return usuariosRepository.findByEmail(email)
            .map(u -> "FOUND: " + u.getEmail() + " (rol=" + u.getRol() + ")")
            .orElse("NOT_FOUND");
    }
}
