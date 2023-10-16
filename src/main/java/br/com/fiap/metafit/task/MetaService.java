package br.com.fiap.metafit.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaService {

    @Autowired
    MetaRepository repository;

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

    public void save(Meta task) {
        repository.save(task);
    }

}
