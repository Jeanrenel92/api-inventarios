package cl.duoc.api_inventarios.Repository;

import cl.duoc.api_inventarios.Model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByFechaBetween(LocalDate inicio, LocalDate fin);
}