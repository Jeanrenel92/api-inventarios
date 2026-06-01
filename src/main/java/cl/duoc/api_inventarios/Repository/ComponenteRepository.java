package cl.duoc.api_inventarios.Repository;

import cl.duoc.api_inventarios.Model.Componente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponenteRepository extends JpaRepository<Componente, Long> {
    boolean existsByFabricanteId(String fabricanteId);
}