package com.fiap.WeatherGuard.repository;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.model.UsuarioAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioAlertaRepository extends JpaRepository<UsuarioAlerta, Long> {
    List<UsuarioAlerta> findByUsuarioOrderByIdDesc(Usuario usuario);

    Optional<UsuarioAlerta> findByUsuarioAndAlerta(Usuario usuario, Alerta alerta);

}
