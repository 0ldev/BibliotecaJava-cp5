package br.com.biblioteca.controller;

import br.com.biblioteca.service.EmprestimoService;
import br.com.biblioteca.service.LivroService;
import br.com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarAtivos());
        return "emprestimos/lista";
    }

    @GetMapping("/todos")
    public String listarTodos(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        model.addAttribute("exibirTodos", true);
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("livrosDisponiveis", livroService.listarDisponiveis());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "emprestimos/formulario";
    }

    @PostMapping
    public String criar(@RequestParam Long livroId,
                       @RequestParam Long usuarioId,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPrevistaDevolucao,
                       RedirectAttributes redirectAttributes) {

        emprestimoService.criarEmprestimo(livroId, usuarioId, dataPrevistaDevolucao);
        redirectAttributes.addFlashAttribute("sucesso", "Empréstimo realizado com sucesso!");
        return "redirect:/emprestimos";
    }

    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        emprestimoService.devolverEmprestimo(id);
        redirectAttributes.addFlashAttribute("sucesso", "Livro devolvido com sucesso!");
        return "redirect:/emprestimos";
    }
}