package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Repository.AuditoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class AuditoriaService {

    private final AuditoriaRepository repository;

    public AuditoriaService(AuditoriaRepository repository) {
        this.repository = repository;
    }


    public List<Auditoria> listarRegistros() {
        log.debug("[Service] Consultando todos los registros de auditoría");
        List<Auditoria> registros = repository.findAll();
        log.debug("[Service] {} registros obtenidos", registros.size());
        return registros;
    }


    public Auditoria buscarRegistroPorId(Long id) {
        log.debug("[Service] Buscando registro id={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Service] Registro no encontrado id={}", id);
                    return new RuntimeException("Registro no encontrado: " + id);
                });
    }


    public Auditoria registrarAccion(Auditoria auditoria) {
        log.info("[Service] Persistiendo auditoría - componenteId={}, estado {} -> {}",
                auditoria.getComponenteId(), auditoria.getEstadoInicial(), auditoria.getEstadoDespues());
        Auditoria guardado = repository.save(auditoria);
        log.info("[Service] Auditoría persistida id={}", guardado.getId());
        return guardado;
    }

    public List<Auditoria> filtrarRegistrosPorFecha(LocalDate inicio, LocalDate fin) {
        log.debug("[Service] Filtrando registros entre {} y {}", inicio, fin);
        List<Auditoria> registros = repository.findByFechaBetween(inicio.atStartOfDay(), fin.atTime(23, 59, 59, 999_999_999));
        log.debug("[Service] {} registros encontrados", registros.size());
        return registros;
    }
}