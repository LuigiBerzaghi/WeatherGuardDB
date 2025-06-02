package com.fiap.WeatherGuard.mapper;

import com.fiap.WeatherGuard.dto.AlertaDTO;
import com.fiap.WeatherGuard.model.UsuarioAlerta;

public class AlertaMapper {

    public static AlertaDTO toDTO(UsuarioAlerta ua) {
        return new AlertaDTO(
                ua.getAlerta().getTipo(),
                ua.getAlerta().getDescricao(),
                ua.getAlerta().getCidade(),
                ua.getAlerta().getData()
        );
    }
}
