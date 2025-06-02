package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.model.UsuarioAlerta;
import com.fiap.WeatherGuard.repository.UsuarioAlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioAlertaService {

    @Autowired
    private UsuarioAlertaRepository usuarioAlertaRepository;

    // Associar um alerta a um usuário
    public UsuarioAlerta associarAlertaAoUsuario(Usuario usuario, Alerta alerta) {
        UsuarioAlerta usuarioAlerta = new UsuarioAlerta();
        usuarioAlerta.setUsuario(usuario);
        usuarioAlerta.setAlerta(alerta);
        usuarioAlerta.setVisualizado(false);
        return usuarioAlertaRepository.save(usuarioAlerta);
    }

    // Listar alertas de um usuário
    public List<UsuarioAlerta> listarAlertasDoUsuario(Usuario usuario) {
        return usuarioAlertaRepository.findByUsuarioOrderByIdDesc(usuario);
    }

}
