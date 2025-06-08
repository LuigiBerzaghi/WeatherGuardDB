package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.repository.AlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    /**
     * Cria novo alerta substituindo qualquer alerta semelhante (tipo, descrição e cidade)
     * criado nas últimas 2 horas.
     */
    public Alerta criarAlerta(String tipo, String descricao, String cidade) {
        LocalDateTime limite = LocalDateTime.now().minusHours(2);
        List<Alerta> alertasRecentes = alertaRepository.findByTipoAndDescricaoAndCidadeAndDataAfter(
                tipo, descricao, cidade, limite
        );

        if (!alertasRecentes.isEmpty()) {
            System.out.println("⚠️ Encontrado(s) " + alertasRecentes.size() + " alerta(s) recente(s) com o mesmo tipo e cidade.");
            alertaRepository.deleteAll(alertasRecentes);
            System.out.println("🗑️ Alertas antigos removidos.");
        }

        Alerta novoAlerta = new Alerta();
        novoAlerta.setTipo(tipo);
        novoAlerta.setDescricao(descricao);
        novoAlerta.setCidade(cidade);
        novoAlerta.setData(LocalDateTime.now());

        Alerta salvo = alertaRepository.save(novoAlerta);
        System.out.println("✅ Novo alerta criado: ID " + salvo.getId() + ", Tipo: " + tipo + ", Cidade: " + cidade);
        return salvo;
    }

    /**
     * Remove todos os alertas com mais de 2 horas
     */
    public void removerAlertasAntigos() {
        LocalDateTime limite = LocalDateTime.now().minusHours(2);
        System.out.println("🧹 Removendo alertas criados antes de: " + limite);
        alertaRepository.deleteByDataBefore(limite);
    }

    public Alerta buscarPorId(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com ID: " + id));
    }

    public List<Alerta> listarPorCidade(String cidade) {
        return alertaRepository.findByCidadeOrderByDataDesc(cidade);
    }

    public List<Alerta> listarTodos() {
        return alertaRepository.findAll();
    }

    public void deletar(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new EntityNotFoundException("Alerta com ID " + id + " não existe.");
        }
        alertaRepository.deleteById(id);
        System.out.println("🗑️ Alerta com ID " + id + " foi deletado.");
    }
}
