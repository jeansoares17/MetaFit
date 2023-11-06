package br.com.fiap.metafit.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.metafit.user.User;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    MetaService service;

    @Autowired
    MessageSource messages;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("username", user.getAttribute("name"));
        model.addAttribute("avatar_url", user.getAttribute("avatar_url"));
        model.addAttribute("metas", service.findAll());
        return "meta/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        if (service.delete(id)) {
            redirect.addFlashAttribute("success", getMessage("meta.delete.success"));
        } else {
            redirect.addFlashAttribute("error", getMessage("meta.notfound"));
        }
        return "redirect:/task";
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

    private String getMessage(String code) {
        return messages.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @GetMapping("dec/{id}")
    public String decrement(@PathVariable Long id) {
        service.decrement(id);
        return "redirect:/meta";
    }

    @GetMapping("inc/{id}")
    public String increment(@PathVariable Long id) {
        service.increment(id);
        return "redirect:/meta";
    }

    @GetMapping("catch/{id}")
    public String catchTask(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.catchTask(id, User.convert(user));
        return "redirect:/meta";
    }

    @GetMapping("drop/{id}")
    public String dropTask(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.dropTask(id, User.convert(user));
        return "redirect:/meta";
    }
}
