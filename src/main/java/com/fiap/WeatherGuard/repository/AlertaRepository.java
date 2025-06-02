package com.fiap.WeatherGuard.repository;

import com.fiap.WeatherGuard.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByCidadeOrderByDataDesc(String cidade);
}
