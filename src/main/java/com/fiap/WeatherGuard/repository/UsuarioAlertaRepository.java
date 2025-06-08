package com.fiap.WeatherGuard.repository;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.model.UsuarioAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioAlertaRepository extends JpaRepository<UsuarioAlerta, Long> {
    List<UsuarioAlerta> findByUsuarioOrderByIdDesc(Usuario usuario);

    Optional<UsuarioAlerta> findByUsuarioAndAlerta(Usuario usuario, Alerta alerta);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM UsuarioAlerta ua WHERE ua.alerta.data < :dataLimite")
    void deleteByAlertaDataBefore(LocalDateTime dataLimite);

}
