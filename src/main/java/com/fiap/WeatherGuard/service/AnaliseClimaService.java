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
        Alerta alerta = analisarClima(resposta, cidade);

        if (alerta != null) {
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
    }

    private Alerta analisarClima(OpenWeatherResponse resposta, String cidade) {
        try {
            double chuva = resposta.getRain() != null && resposta.getRain().get("1h") != null
                    ? ((Number) resposta.getRain().get("1h")).doubleValue() : 0;
            double vento = resposta.getWind() != null && resposta.getWind().get("speed") != null
                    ? ((Number) resposta.getWind().get("speed")).doubleValue() : 0;
            double temperatura = resposta.getMain() != null && resposta.getMain().get("temp") != null
                    ? ((Number) resposta.getMain().get("temp")).doubleValue() : 0;

            // 🔴 Ordem de prioridade: Chuva > Vento > Calor > Frio
            if (chuva > 50) return criarAlerta("Chuva Extrema", "Risco severo de alagamentos", cidade);
            if (chuva > 25) return criarAlerta("Chuva Intensa", "Risco de alagamentos", cidade);
            if (chuva > 10) return criarAlerta("Chuva Moderada", "Volume de chuva acima do normal", cidade);

            if (vento > 80) return criarAlerta("Vendaval Severo", "Risco extremo de vendaval", cidade);
            if (vento > 60) return criarAlerta("Vendaval", "Risco de vendaval detectado", cidade);
            if (vento > 40) return criarAlerta("Vento Forte", "Rajadas de vento intensas", cidade);

            if (temperatura > 40) return criarAlerta("Onda de Calor", "Risco de calor extremo detectado", cidade);
            if (temperatura > 35) return criarAlerta("Calor Intenso", "Temperatura muito elevada", cidade);
            if (temperatura > 30) return criarAlerta("Calor Moderado", "Temperatura acima do ideal", cidade);

            if (temperatura < 0) return criarAlerta("Congelamento", "Risco de congelamento", cidade);
            if (temperatura < 5) return criarAlerta("Frio Intenso", "Temperatura muito baixa", cidade);
            if (temperatura < 10) return criarAlerta("Frio Moderado", "Temperatura abaixo do ideal", cidade);

        } catch (Exception e) {
            System.err.println("🚨 Erro ao analisar clima: " + e.getMessage());
        }

        return null; // Nenhuma condição severa
    }

    private Alerta criarAlerta(String tipo, String descricao, String cidade) {
        System.out.println("🌦️ Clima identificado: " + tipo + " - " + descricao + " - Cidade: " + cidade);
        return alertaService.criarAlerta(tipo, descricao, cidade);
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
