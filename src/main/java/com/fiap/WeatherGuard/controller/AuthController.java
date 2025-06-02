package com.fiap.WeatherGuard.controller;

import com.fiap.WeatherGuard.dto.JwtDTO;
import com.fiap.WeatherGuard.dto.LoginDTO;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.service.UsuarioService;
import com.fiap.WeatherGuard.util.JwtUtil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody @Valid LoginDTO login) {
        Usuario usuario = usuarioService.buscarPorEmail(login.getEmail());

        if (!usuario.getSenha().equals(login.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        return ResponseEntity.ok(new JwtDTO("Bearer " + token));
    }
}
