package com.fiap.WeatherGuard.service;

import com.fiap.WeatherGuard.model.Usuario;
import com.fiap.WeatherGuard.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    // Buscar usuário por e-mail
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    // Cadastrar novo usuário
    public Usuario cadastrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Atualizar dados do usuário
    public Usuario atualizar(Long id, Usuario atualizado) {
        Usuario existente = buscarPorId(id);
        existente.setNome(atualizado.getNome());
        existente.setEmail(atualizado.getEmail());
        existente.setSenha(atualizado.getSenha());
        existente.setCidade(atualizado.getCidade());
        return usuarioRepository.save(existente);
    }

    // Deletar usuário
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário com ID " + id + " não existe.");
        }
        usuarioRepository.deleteById(id);
    }
    
    // Listar todos os usuários com paginação
    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    // Listar todos os usuários de uma determinada cidade com paginação
    public Page<Usuario> buscarPorCidade(String cidade, Pageable pageable) {
        return usuarioRepository.findByCidadeIgnoreCaseContaining(cidade, pageable);
    }

}
