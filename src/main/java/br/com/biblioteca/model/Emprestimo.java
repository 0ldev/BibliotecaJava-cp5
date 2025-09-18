package br.com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    @NotNull(message = "{emprestimo.livro.obrigatorio}")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "{emprestimo.usuario.obrigatorio}")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate dataRetirada;

    @NotNull(message = "{emprestimo.dataPrevista.obrigatoria}")
    @Column(nullable = false)
    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO;

    public Emprestimo() {}

    public Emprestimo(Livro livro, Usuario usuario, LocalDate dataPrevistaDevolucao) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = LocalDate.now();
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.status = StatusEmprestimo.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return StatusEmprestimo.ATIVO.equals(this.status);
    }

    public boolean isVencido() {
        return isAtivo() && dataPrevistaDevolucao.isBefore(LocalDate.now());
    }

    @PrePersist
    private void prePersist() {
        if (dataRetirada == null) {
            dataRetirada = LocalDate.now();
        }
    }
}