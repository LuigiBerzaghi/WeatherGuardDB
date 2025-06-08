package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.repository.AlertaRepository;
import com.fiap.WeatherGuard.repository.UsuarioAlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private UsuarioAlertaRepository usuarioAlertaRepository;

    /**
     * Cria novo alerta substituindo qualquer alerta semelhante (tipo, descrição e cidade)
     * criado nas últimas 6 horas.
     */
    public Alerta criarAlerta(String tipo, String descricao, String cidade) {
        LocalDateTime limite = LocalDateTime.now().minusHours(6);
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
     * Remove todos os alertas com mais de 6 horas,
     * incluindo seus relacionamentos em UsuarioAlerta para evitar erros de integridade.
     */
    @Transactional
    public void removerAlertasAntigos() {
        LocalDateTime limite = LocalDateTime.now().minusHours(6);
        System.out.println("🧹 Removendo alertas criados antes de: " + limite);

        try {
            // Busca alertas antigos antes de deletar
            List<Alerta> alertasAntigos = alertaRepository.findByDataBefore(limite);

            if (alertasAntigos.isEmpty()) {
                System.out.println("Nenhum alerta antigo para remover.");
                return;
            }

            // Mostra quais alertas serão apagados
            System.out.println("Alertas a serem removidos:");
            alertasAntigos.forEach(alerta -> 
                System.out.println(" - ID: " + alerta.getId() + ", Tipo: " + alerta.getTipo() + ", Cidade: " + alerta.getCidade() + ", Data: " + alerta.getData())
            );

            // Deleta os relacionamentos dos alertas antigos primeiro
            usuarioAlertaRepository.deleteByAlertaDataBefore(limite);

            // Depois deleta os alertas antigos
            alertaRepository.deleteByDataBefore(limite);

            System.out.println("✅ Alertas antigos removidos com sucesso.");

        } catch (Exception e) {
            System.err.println("❌ Erro ao tentar remover alertas antigos: " + e.getMessage());
            e.printStackTrace();
        }
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
