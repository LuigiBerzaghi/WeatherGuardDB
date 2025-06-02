package com.fiap.WeatherGuard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    @JsonProperty("main")
    private Map<String, Object> main;

    @JsonProperty("wind")
    private Map<String, Object> wind;

    @JsonProperty("rain")
    private Map<String, Object> rain;

    @JsonProperty("coord")
    private Map<String, Object> coord;

    @JsonProperty("name")
    private String cidade;

    public Map<String, Object> getMain() {
        return main;
    }

    public Map<String, Object> getWind() {
        return wind;
    }

    public Map<String, Object> getRain() {
        return rain;
    }

    public Map<String, Object> getCoord() {
        return coord;
    }

    public String getCidade() {
        return cidade;
    }
}
