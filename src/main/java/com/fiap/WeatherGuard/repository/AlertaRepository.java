package com.fiap.WeatherGuard.repository;

import com.fiap.WeatherGuard.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByCidadeOrderByDataDesc(String cidade);
    List<Alerta> findByTipoAndCidadeOrderByDataDesc(String tipo, String cidade);
    List<Alerta> findByTipoAndDescricaoAndCidadeAndDataAfter(
        String tipo, String descricao, String cidade, LocalDateTime data
    );

    Optional<Alerta> findFirstByTipoAndDescricaoAndCidadeOrderByDataDesc(
        String tipo, String descricao, String cidade
    );

    void deleteByDataBefore(LocalDateTime dataLimite);
}