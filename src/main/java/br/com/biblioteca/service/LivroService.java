package br.com.biblioteca.service;

import br.com.biblioteca.exception.RegraNegocioException;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.StatusLivro;
import br.com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public List<Livro> listarDisponiveis() {
        return livroRepository.findByStatus(StatusLivro.DISPONIVEL);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public Livro atualizar(Long id, Livro livroAtualizado) {
        Optional<Livro> livroExistente = livroRepository.findById(id);
        if (livroExistente.isEmpty()) {
            throw new RegraNegocioException("Livro não encontrado");
        }

        Livro livro = livroExistente.get();
        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setAutor(livroAtualizado.getAutor());
        livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());

        return livroRepository.save(livro);
    }

    public void excluir(Long id) {
        Optional<Livro> livroOpt = livroRepository.findById(id);
        if (livroOpt.isEmpty()) {
            throw new RegraNegocioException("Livro não encontrado");
        }

        // Exclui o livro (cascade irá excluir todos os empréstimos automaticamente)
        livroRepository.deleteById(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }
}