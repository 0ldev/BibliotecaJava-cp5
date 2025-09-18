package br.com.biblioteca.service;

import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.*;
import br.com.biblioteca.repository.EmprestimoRepository;
import br.com.biblioteca.repository.LivroRepository;
import br.com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Emprestimo> listarAtivos() {
        return emprestimoRepository.findByStatusOrderByDataRetiradaDesc(StatusEmprestimo.ATIVO);
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> listarVencidos() {
        return emprestimoRepository.findByStatusAndDataPrevistaDevolucaoBefore(
                StatusEmprestimo.ATIVO, LocalDate.now());
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo criarEmprestimo(Long livroId, Long usuarioId, LocalDate dataPrevistaDevolucao) {
        if (dataPrevistaDevolucao == null) {
            throw new RegraNegocioException("Data prevista de devolução é obrigatória");
        }

        if (dataPrevistaDevolucao.isBefore(LocalDate.now()) ||
            dataPrevistaDevolucao.isEqual(LocalDate.now())) {
            throw new RegraNegocioException("Data prevista de devolução deve ser posterior à data atual");
        }

        Optional<Livro> livroOpt = livroRepository.findById(livroId);
        if (livroOpt.isEmpty()) {
            throw new RegraNegocioException("Livro não encontrado");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado");
        }

        Livro livro = livroOpt.get();
        Usuario usuario = usuarioOpt.get();

        if (!livro.isDisponivel()) {
            throw new RegraNegocioException("Livro não está disponível para empréstimo");
        }

        boolean temEmprestimoAtivo = emprestimoRepository.existsByLivroAndStatus(livro, StatusEmprestimo.ATIVO);
        if (temEmprestimoAtivo) {
            throw new RegraNegocioException("Livro já possui empréstimo ativo");
        }

        livro.setStatus(StatusLivro.EMPRESTADO);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo(livro, usuario, dataPrevistaDevolucao);
        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo devolverEmprestimo(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findById(emprestimoId);
        if (emprestimoOpt.isEmpty()) {
            throw new RegraNegocioException("Empréstimo não encontrado");
        }

        Emprestimo emprestimo = emprestimoOpt.get();

        if (!emprestimo.isAtivo()) {
            throw new RegraNegocioException("Empréstimo já foi devolvido");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        Livro livro = emprestimo.getLivro();
        livro.setStatus(StatusLivro.DISPONIVEL);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuário não encontrado");
        }
        return emprestimoRepository.findByUsuario(usuario.get());
    }
}