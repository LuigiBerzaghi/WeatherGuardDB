package com.fiap.WeatherGuard.controller;

import com.fiap.WeatherGuard.dto.UsuarioDTO;

import com.fiap.WeatherGuard.mapper.UsuarioMapper;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.service.UsuarioService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos os usuários
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String cidade // filtro opcional
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Usuario> usuarios;

        if (cidade != null && !cidade.isEmpty()) {
            usuarios = usuarioService.buscarPorCidade(cidade, pageable);
        } else {
            usuarios = usuarioService.listarTodos(pageable);
        }

        Page<UsuarioDTO> dtoPage = usuarios.map(UsuarioMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioDTO dto = UsuarioMapper.toDTO(usuario);
        return ResponseEntity.ok(dto);
    }


    // Cadastrar novo usuário
    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrar(@Valid @RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        UsuarioDTO dto = UsuarioMapper.toDTO(novoUsuario);
        return ResponseEntity.status(201).body(dto);
    }


    // Atualizar dados do usuário
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        Usuario atualizado = usuarioService.atualizar(id, usuario);
        UsuarioDTO dto = UsuarioMapper.toDTO(atualizado);
        return ResponseEntity.ok(dto);
    }

    // Deletar usuário
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    // Retornar dados do usuário logado
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> me(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }
}
