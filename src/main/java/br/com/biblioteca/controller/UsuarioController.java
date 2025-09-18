package br.com.biblioteca.controller;

import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Usuario usuario, BindingResult result,
                        RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário salvo com sucesso!");
        return "redirect:/usuarios";
    }
}