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

    // Criar novo alerta
    public Alerta criarAlerta(String tipo, String descricao, String cidade) {
        Alerta alerta = new Alerta();
        alerta.setTipo(tipo);
        alerta.setDescricao(descricao);
        alerta.setCidade(cidade);
        alerta.setData(LocalDateTime.now());
        return alertaRepository.save(alerta);
    }

    // Buscar alerta por ID
    public Alerta buscarPorId(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com ID: " + id));
    }

    // Buscar todos os alertas de uma cidade
    public List<Alerta> listarPorCidade(String cidade) {
        return alertaRepository.findByCidadeOrderByDataDesc(cidade);
    }

    // Listar todos os alertas
    public List<Alerta> listarTodos() {
        return alertaRepository.findAll();
    }

    // Deletar alerta
    public void deletar(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new EntityNotFoundException("Alerta com ID " + id + " não existe.");
        }
        alertaRepository.deleteById(id);
    }
}
