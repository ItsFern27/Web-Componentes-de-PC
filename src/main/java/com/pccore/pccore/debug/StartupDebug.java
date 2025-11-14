package com.pccore.pccore.debug;

import com.pccore.pccore.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StartupDebug implements CommandLineRunner {

    private final UsuariosRepository usuariosRepository;
    private final Logger log = LoggerFactory.getLogger(StartupDebug.class);

    @Override
    public void run(String... args) throws Exception {
        long total = usuariosRepository.count();
        log.info("[startup-debug] Total usuarios en BD: {}", total);
        usuariosRepository.findAll().stream().limit(10)
            .map(u -> u.getEmail())
            .collect(Collectors.toList())
            .forEach(e -> log.info("[startup-debug] usuario: {}", e));
    }
}
