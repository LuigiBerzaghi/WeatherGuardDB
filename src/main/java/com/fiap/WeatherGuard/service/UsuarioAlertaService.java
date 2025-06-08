package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.model.Alerta;
import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.model.UsuarioAlerta;
import com.fiap.WeatherGuard.repository.UsuarioAlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioAlertaService {

    @Autowired
    private UsuarioAlertaRepository usuarioAlertaRepository;

    public UsuarioAlerta associarAlertaAoUsuario(Usuario usuario, Alerta alerta) {
        String cidadeUsuario = normalize(usuario.getCidade());
        String cidadeAlerta = normalize(alerta.getCidade());

        System.out.println("📍 Verificando associação:");
        System.out.println("   ➤ Cidade do usuário: [" + usuario.getCidade() + "] → Normalizada: [" + cidadeUsuario + "]");
        System.out.println("   ➤ Cidade do alerta : [" + alerta.getCidade() + "] → Normalizada: [" + cidadeAlerta + "]");

        if (!cidadeUsuario.equals(cidadeAlerta)) {
            System.out.println("❌ Associação ignorada: cidades diferentes após normalização.");
            return null;
        }

        Optional<UsuarioAlerta> existente = usuarioAlertaRepository.findByUsuarioAndAlerta(usuario, alerta);
        if (existente.isPresent()) {
            System.out.println("ℹ️ Associação já existente: Usuário ID " + usuario.getId() + ", Alerta ID " + alerta.getId());
            return existente.get();
        }

        System.out.println("✅ Criando nova associação: Usuário ID " + usuario.getId() + ", Alerta ID " + alerta.getId());
        UsuarioAlerta usuarioAlerta = new UsuarioAlerta();
        usuarioAlerta.setUsuario(usuario);
        usuarioAlerta.setAlerta(alerta);
        usuarioAlerta.setVisualizado(false);

        UsuarioAlerta salvo = usuarioAlertaRepository.save(usuarioAlerta);
        System.out.println("💾 Associação salva com ID: " + salvo.getId());
        return salvo;
    }

    public List<UsuarioAlerta> listarAlertasDoUsuario(Usuario usuario) {
        return usuarioAlertaRepository.findByUsuarioOrderByIdDesc(usuario);
    }

    private String normalize(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .trim()
                .toLowerCase();
    }
}
