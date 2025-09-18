package br.com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{usuario.nome.obrigatorio}")
    @Column(nullable = false)
    private String nome;

    @Email(message = "{usuario.email.invalido}")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Emprestimo> emprestimos;

    public Usuario() {}

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public long getQuantidadeEmprestimosAtivos() {
        if (emprestimos == null) return 0;
        return emprestimos.stream()
                .filter(emprestimo -> StatusEmprestimo.ATIVO.equals(emprestimo.getStatus()))
                .count();
    }
}