package com.fiap.WeatherGuard.repository;

import com.fiap.WeatherGuard.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Alerta a WHERE a.data < :dataLimite")
    void deleteByDataBefore(@Param("dataLimite") LocalDateTime dataLimite);
	List<Alerta> findByDataBefore(LocalDateTime limite);
}