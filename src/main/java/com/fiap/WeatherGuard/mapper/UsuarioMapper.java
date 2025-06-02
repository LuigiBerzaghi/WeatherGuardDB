package com.fiap.WeatherGuard.mapper;

import com.fiap.WeatherGuard.dto.UsuarioDTO;
import com.fiap.WeatherGuard.model.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCidade()
        );
    }
}
