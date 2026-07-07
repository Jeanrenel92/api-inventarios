package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository repository;

    public AuditoriaService(AuditoriaRepository repository) {
        this.repository = repository;
    }


    public List<Auditoria> listarTodosAudit() {
        return repository.findAll();
    }



    public Auditoria buscarAuditPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auditoria no encontrada: " + id));
    }



    public Auditoria crearAuditoria(Auditoria auditoria) {
        return repository.save(auditoria);
    }

    public Auditoria actualizarAuditoria(Long id, Auditoria auditoria) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setId(id);
                    return repository.save(auditoria);
                })
                .orElseThrow(() -> new RuntimeException("Audiotria con id=" + id + " no encontrada"));

    }

    public void eliminarAudit(Long id) {
        repository.deleteById(id);
    }

    public List<Auditoria> filtrarAuditPorFecha(LocalDate inicio, LocalDate fin) {
        return repository.findByFechaBetween(inicio, fin);
    }
}