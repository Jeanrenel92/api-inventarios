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


    public List<Auditoria> listarRegistros() {
        return repository.findAll();
    }


    public Auditoria buscarRegistroPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado: " + id));
    }


    public Auditoria registrarAccion(Auditoria auditoria) {
        return repository.save(auditoria);
    }

    public List<Auditoria> filtrarRegistrosPorFecha(LocalDate inicio, LocalDate fin) {
        return repository.findByFechaBetween(inicio.atStartOfDay(), fin.atTime(23, 59, 59, 999_999_999));
    }

    Caused by: java.sql.SQLException: ORA-12506: TNS: el listener ha rechazado la conexión según el filtrado ACL del servicio (CONNECTION_ID=CJ0DqX9PRYenETqRiKapLg==
}
