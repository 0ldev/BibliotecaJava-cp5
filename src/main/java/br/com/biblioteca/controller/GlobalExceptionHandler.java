package br.com.biblioteca.controller;

import br.com.biblioteca.exception.RegraNegocioException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public String handleRegraNegocioException(RegraNegocioException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("erro", "Ocorreu um erro interno. Tente novamente.");
        return "error";
    }
}