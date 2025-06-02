package com.fiap.WeatherGuard.controller;

import com.fiap.WeatherGuard.dto.AlertaDTO;
import com.fiap.WeatherGuard.mapper.AlertaMapper;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.model.UsuarioAlerta;
import com.fiap.WeatherGuard.service.UsuarioAlertaService;
import com.fiap.WeatherGuard.service.UsuarioService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/usuario-alertas")
public class UsuarioAlertaController {

    @Autowired
    private UsuarioAlertaService usuarioAlertaService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar alertas de um usuário 
    @GetMapping("/usuario/me")
    public ResponseEntity<List<AlertaDTO>> listarMeusAlertas(@AuthenticationPrincipal Usuario usuarioAutenticado) {
        List<UsuarioAlerta> alertas = usuarioAlertaService.listarAlertasDoUsuario(usuarioAutenticado);
        List<AlertaDTO> resposta = alertas.stream()
                .map(AlertaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(resposta);
    }

}
