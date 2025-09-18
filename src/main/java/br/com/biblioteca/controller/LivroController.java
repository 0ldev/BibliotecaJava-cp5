package br.com.biblioteca.controller;

import br.com.biblioteca.model.Livro;
import br.com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        return "livros/lista";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model) {
        model.addAttribute("livros", livroService.listarDisponiveis());
        model.addAttribute("apenasDisponiveis", true);
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("livro", new Livro());
        return "livros/formulario";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Livro livro, BindingResult result,
                        RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "livros/formulario";
        }

        livroService.salvar(livro);
        redirectAttributes.addFlashAttribute("sucesso", "Livro salvo com sucesso!");
        return "redirect:/livros";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Livro> livro = livroService.buscarPorId(id);
        if (livro.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Livro não encontrado");
            return "redirect:/livros";
        }

        model.addAttribute("livro", livro.get());
        return "livros/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Livro livro,
                           BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "livros/formulario";
        }

        livroService.atualizar(id, livro);
        redirectAttributes.addFlashAttribute("sucesso", "Livro atualizado com sucesso!");
        return "redirect:/livros";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            livroService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/livros";
    }
}