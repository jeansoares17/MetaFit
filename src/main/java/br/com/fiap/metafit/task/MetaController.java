package br.com.fiap.metafit.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    MetaService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("metas", service.findAll());
        return "meta/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        if (service.delete(id)) {
            redirect.addFlashAttribute("success", "Meta apagada com sucesso");
        } else {
            redirect.addFlashAttribute("error", "Meta não encontrada");
        }
        return "redirect:/meta";
    }

    @GetMapping("new")
    public String form(Meta meta) {
        return "meta/form";
    }

    @PostMapping
    public String create(@Valid Meta meta, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors())
            return "meta/form";
        service.save(meta);
        redirect.addFlashAttribute("success", "Meta cadastrada com sucesso");
        return "redirect:/meta";
    }

}
