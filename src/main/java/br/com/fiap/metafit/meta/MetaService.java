package br.com.fiap.metafit.meta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import br.com.fiap.metafit.user.User;
import br.com.fiap.metafit.user.UserService;

@Service
public class MetaService {

    @Autowired
    MetaRepository repository;

    @Autowired
    UserService userService;

    public List<Meta> findAll() {
        return repository.findAll();
    }

    public boolean delete(Long id) {
        var task = repository.findById(id);
        if (task.isEmpty())
            return false;
        repository.deleteById(id);
        return true;
    }

    public void save(Meta meta) {
        repository.save(meta);
    }

    public void decrement(Long id) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("meta não encontrada");

        var meta = optional.get();
        if (meta.getStatus() == 0)
            throw new RuntimeException("status não pode ser negativo");

        meta.setStatus(meta.getStatus() - 10);
        repository.save(meta);
    }

    public void increment(Long id) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("meta não encontrada");

        var meta = optional.get();
        if (meta.getStatus() == 100)
            throw new RuntimeException("status não pode maior que 100%");

        meta.setStatus(meta.getStatus() + 10);

        if (meta.getStatus() == 100) {
            var user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.addScore(User.convert(user), meta.getScore());
        }

        repository.save(meta);
    }

    public void catchTask(Long id, User user) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("meta não encontrada");

        var meta = optional.get();
        if (meta.getUser() != null)
            throw new RuntimeException("meta já atribuída");

        meta.setUser(user);
        repository.save(meta);
    }

    public void dropTask(Long id, User user) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("meta não encontrada");

        var meta = optional.get();
        if (meta.getUser() == null)
            throw new RuntimeException("meta não atribuída");

        if (!meta.getUser().equals(user))
            throw new RuntimeException("não pode largar meta de outro usuário");

        meta.setUser(null);
        repository.save(meta);
    }
}
