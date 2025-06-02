package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.client.ClimaClient;
import com.fiap.WeatherGuard.dto.OpenWeatherResponse;
import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.service.AlertaService;
import com.fiap.WeatherGuard.service.UsuarioService;
import com.fiap.WeatherGuard.service.UsuarioAlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnaliseClimaService {

    @Autowired
    private ClimaClient climaClient;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private UsuarioAlertaService usuarioAlertaService;

    public void verificarClimaPorLocalizacao(double lat, double lon) {
        OpenWeatherResponse resposta = climaClient.buscarClima(lat, lon);
        if (resposta == null || resposta.getCidade() == null) return;

        String cidade = resposta.getCidade();
        String tipo = null;
        String descricao = null;

        try {
            double chuva = resposta.getRain() != null && resposta.getRain().get("1h") != null
                    ? ((Number) resposta.getRain().get("1h")).doubleValue() : 0;
            double vento = resposta.getWind() != null && resposta.getWind().get("speed") != null
                    ? ((Number) resposta.getWind().get("speed")).doubleValue() : 0;
            double temperatura = resposta.getMain() != null && resposta.getMain().get("temp") != null
                    ? ((Number) resposta.getMain().get("temp")).doubleValue() : 0;

            if (chuva > 25) {
                tipo = "Alagamento";
                descricao = "Risco de alagamento detectado";
            } else if (vento > 60) {
                tipo = "Vendaval";
                descricao = "Risco de vendaval detectado";
            } else if (temperatura > 35) {
                tipo = "Onda de Calor";
                descricao = "Risco de calor extremo detectado";
            }

            if (tipo != null) {
                Alerta alerta = alertaService.criarAlerta(tipo, descricao, cidade);

                List<Usuario> usuarios = usuarioService.listarTodos();
                for (Usuario usuario : usuarios) {
                    if (usuario.getCidade().equalsIgnoreCase(cidade)) {
                        usuarioAlertaService.associarAlertaAoUsuario(usuario, alerta);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao analisar clima: " + e.getMessage());
        }
    }

	@Scheduled(fixedRate = 1800000) // a cada 30 minutos (em milissegundos)
	public void analisePeriodica() {
	    List<Usuario> usuarios = usuarioService.listarTodos();
	
	    for (Usuario usuario : usuarios) {
	        try {
	            OpenWeatherResponse resposta = climaClient.buscarClimaPorCidade(usuario.getCidade());
	
	            if (resposta != null && resposta.getCidade() != null) {
	                verificarClimaPorLocalizacao(
	                    ((Number) resposta.getCoord().get("lat")).doubleValue(),
	                    ((Number) resposta.getCoord().get("lon")).doubleValue()
	                );
	            }
	        } catch (Exception e) {
	            System.err.println("Erro ao agendar análise para cidade " + usuario.getCidade() + ": " + e.getMessage());
	        }
	    }
	
	    System.out.println("✔️ Análise climática agendada executada.");
	}
}
