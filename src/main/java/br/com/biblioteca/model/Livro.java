package br.com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{livro.titulo.obrigatorio}")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "{livro.autor.obrigatorio}")
    @Column(nullable = false)
    private String autor;

    @Min(value = 0, message = "{livro.ano.invalido}")
    @Max(value = 2025, message = "{livro.ano.invalido}")
    private Integer anoPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLivro status = StatusLivro.DISPONIVEL;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprestimo> emprestimos;

    public Livro() {}

    public Livro(String titulo, String autor, Integer anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.status = StatusLivro.DISPONIVEL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public StatusLivro getStatus() {
        return status;
    }

    public void setStatus(StatusLivro status) {
        this.status = status;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public boolean isDisponivel() {
        return StatusLivro.DISPONIVEL.equals(this.status);
    }

    public boolean podeSerExcluido() {
        return true; // Todos os livros podem ser excluídos
    }

    @PrePersist
    @PreUpdate
    private void validarAnoPublicacao() {
        if (anoPublicacao != null && anoPublicacao > LocalDate.now().getYear()) {
            this.anoPublicacao = LocalDate.now().getYear();
        }
    }
}