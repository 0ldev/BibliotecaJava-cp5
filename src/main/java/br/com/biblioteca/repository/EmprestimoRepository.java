package br.com.biblioteca.repository;

import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.StatusEmprestimo;
import br.com.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    boolean existsByLivroAndStatus(Livro livro, StatusEmprestimo status);

    List<Emprestimo> findByUsuario(Usuario usuario);

    List<Emprestimo> findByStatusAndDataPrevistaDevolucaoBefore(StatusEmprestimo status, LocalDate data);

    @Query("SELECT e FROM Emprestimo e WHERE e.status = :status ORDER BY e.dataRetirada DESC")
    List<Emprestimo> findByStatusOrderByDataRetiradaDesc(StatusEmprestimo status);
}