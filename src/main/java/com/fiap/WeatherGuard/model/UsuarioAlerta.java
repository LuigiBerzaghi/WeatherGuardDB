package com.fiap.WeatherGuard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuario_alertas")
public class UsuarioAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_alerta_seq")
    @SequenceGenerator(name = "usuario_alerta_seq", sequenceName = "USUARIO_ALERTA_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alerta_id", nullable = false)
    @NotNull(message = "Alerta é obrigatório")
    private Alerta alerta;

    private boolean visualizado = false;

    // Construtores
    public UsuarioAlerta() {}

    public UsuarioAlerta(Usuario usuario, Alerta alerta, boolean visualizado) {
        this.usuario = usuario;
        this.alerta = alerta;
        this.visualizado = visualizado;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }
}
