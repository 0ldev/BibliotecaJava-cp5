package br.com.biblioteca.repository;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.StatusLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByStatus(StatusLivro status);

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorContainingIgnoreCase(String autor);

}