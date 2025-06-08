package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.client.ClimaClient;
import com.fiap.WeatherGuard.dto.OpenWeatherResponse;
import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
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
        if (resposta == null || resposta.getCidade() == null) {
            System.out.println("❌ Resposta inválida ou sem cidade.");
            return;
        }

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

            // --- CHUVA ---
            if (chuva > 50) {
                tipo = "Chuva Extrema";
                descricao = "Risco severo de alagamentos";
            } else if (chuva > 25) {
                tipo = "Chuva Intensa";
                descricao = "Risco de alagamentos";
            } else if (chuva > 10) {
                tipo = "Chuva Moderada";
                descricao = "Volume de chuva acima do normal";
            }

            // --- VENTO ---
            else if (vento > 80) {
                tipo = "Vendaval Severo";
                descricao = "Risco extremo de vendaval";
            } else if (vento > 60) {
                tipo = "Vendaval";
                descricao = "Risco de vendaval detectado";
            } else if (vento > 40) {
                tipo = "Vento Forte";
                descricao = "Rajadas de vento intensas";
            }

            // --- TEMPERATURA (CALOR) ---
            else if (temperatura > 40) {
                tipo = "Onda de Calor";
                descricao = "Risco de calor extremo detectado";
            } else if (temperatura > 35) {
                tipo = "Calor Intenso";
                descricao = "Temperatura muito elevada";
            } else if (temperatura > 30) {
                tipo = "Calor Moderado";
                descricao = "Temperatura acima do ideal";
            }

            // --- TEMPERATURA (FRIO) ---
            else if (temperatura < 0) {
                tipo = "Congelamento";
                descricao = "Risco de congelamento";
            } else if (temperatura < 5) {
                tipo = "Frio Intenso";
                descricao = "Temperatura muito baixa";
            } else if (temperatura < 10) {
                tipo = "Frio Moderado";
                descricao = "Temperatura abaixo do ideal";
            }

            if (tipo != null) {
                System.out.println("🌦️ Clima identificado: " + tipo + " - " + descricao + " - Cidade: " + cidade);

                // Cria novo alerta (substituindo os antigos automaticamente)
                Alerta alerta = alertaService.criarAlerta(tipo, descricao, cidade);

                List<Usuario> usuarios = usuarioService.listarTodos();
                System.out.println("👥 Total de usuários: " + usuarios.size());

                for (Usuario usuario : usuarios) {
                    System.out.println("➡️ Verificando usuário ID: " + usuario.getId() + " - Cidade: " + usuario.getCidade());

                    if (usuario.getCidade().equalsIgnoreCase(cidade)) {
                        System.out.println("✅ Correspondência de cidade. Associando alerta...");
                        usuarioAlertaService.associarAlertaAoUsuario(usuario, alerta);
                    } else {
                        System.out.println("❌ Cidade não corresponde: " + usuario.getCidade() + " != " + cidade);
                    }
                }
            } else {
                System.out.println("ℹ️ Nenhuma condição climática extrema identificada.");
            }

        } catch (Exception e) {
            System.err.println("🚨 Erro ao analisar clima: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 1800000) // a cada 30 minutos
    public void analisePeriodica() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        System.out.println("🕒 Iniciando análise climática periódica...");

        for (Usuario usuario : usuarios) {
            try {
                OpenWeatherResponse resposta = climaClient.buscarClimaPorCidade(usuario.getCidade());

                if (resposta != null && resposta.getCidade() != null) {
                    verificarClimaPorLocalizacao(
                            ((Number) resposta.getCoord().get("lat")).doubleValue(),
                            ((Number) resposta.getCoord().get("lon")).doubleValue()
                    );
                } else {
                    System.out.println("❌ Clima inválido para usuário: " + usuario.getId());
                }
            } catch (Exception e) {
                System.err.println("🚨 Erro ao agendar análise para cidade " + usuario.getCidade() + ": " + e.getMessage());
            }
        }

        alertaService.removerAlertasAntigos();
        System.out.println("✔️ Análise climática agendada concluída.");
    }
}
