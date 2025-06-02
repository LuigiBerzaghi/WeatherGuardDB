package com.fiap.WeatherGuard.controller;

import com.fiap.WeatherGuard.service.AnaliseClimaService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    @Autowired
    private AnaliseClimaService analiseClimaService;

    //Endpoint para fazer análise climática manualmente
    @GetMapping("/analisar")
    public ResponseEntity<String> analisarClima(@RequestParam double lat, @RequestParam double lon) {
        analiseClimaService.verificarClimaPorLocalizacao(lat, lon);
        return ResponseEntity.ok("Análise climática concluída com sucesso.");
    }
}
