package ps2.titular_app;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TitularDao {

    private final TitularRepo repo;

    public TitularDao(TitularRepo repo) {
        this.repo = repo;
    }

    public List<Titular> listarTodos() {
        List<Titular> out = new ArrayList<>();
        repo.findAll().forEach(out::add);
        return out;
    }

    public Titular buscarPorId(long id) {
        Optional<Titular> opt = repo.findById(id);
        return opt.orElse(null);
    }

    public Titular criar(Titular t) {
        return repo.save(t);
    }

    public Titular atualizar(Titular t) {
        if (t.getId() == 0 || !repo.existsById(t.getId())) {
            throw new IllegalArgumentException("Titular não encontrado para atualizar (id=" + t.getId() + ").");
        }
        return repo.save(t);
    }

    public void apagar(long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Titular não encontrado para apagar (id=" + id + ").");
        }
        repo.deleteById(id);
    }
}