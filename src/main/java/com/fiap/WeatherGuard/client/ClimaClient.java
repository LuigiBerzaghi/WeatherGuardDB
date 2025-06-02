package com.fiap.WeatherGuard.client;

import com.fiap.WeatherGuard.dto.OpenWeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ClimaClient {

    private final WebClient webClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    public ClimaClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.openweathermap.org/data/2.5").build();
    }

    public OpenWeatherResponse buscarClima(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "pt_br")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherResponse.class)
                .onErrorResume(ex -> {
                    System.err.println("Erro ao consumir a API do clima: " + ex.getMessage());
                    return Mono.empty();
                })
                .block();
    }
    public OpenWeatherResponse buscarClimaPorCidade(String cidade) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/weather")
                .queryParam("q", cidade)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "pt_br")
                .build())
            .retrieve()
            .bodyToMono(OpenWeatherResponse.class)
            .onErrorResume(ex -> {
                System.err.println("Erro ao buscar clima por cidade: " + ex.getMessage());
                return Mono.empty();
            })
            .block();
    }
}
