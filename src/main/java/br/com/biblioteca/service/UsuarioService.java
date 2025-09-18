package br.com.biblioteca.service;

import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RegraNegocioException("E-mail já está em uso por outro usuário");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado");
        }

        Usuario usuario = usuarioExistente.get();

        if (usuarioAtualizado.getEmail() != null &&
            !usuarioAtualizado.getEmail().equals(usuario.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())) {
            throw new RegraNegocioException("E-mail já está em uso por outro usuário");
        }

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());

        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}