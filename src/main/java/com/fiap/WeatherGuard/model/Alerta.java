package com.fiap.WeatherGuard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alerta_seq")
    @SequenceGenerator(name = "alerta_seq", sequenceName = "ALERTA_SEQ", allocationSize = 1)
    private Long id;

    @NotBlank(message = "O tipo do alerta é obrigatório")
    private String tipo; 

    @NotBlank(message = "A descrição do alerta é obrigatória")
    private String descricao;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotNull(message = "A data do alerta é obrigatória")
    private LocalDateTime data;
    
    @JsonIgnore
    @OneToMany(mappedBy = "alerta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioAlerta> usuarios;

    // Construtores
    public Alerta() {}

    public Alerta(String tipo, String descricao, String cidade, LocalDateTime data) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.cidade = cidade;
        this.data = data;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}

