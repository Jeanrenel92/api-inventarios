package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Model.ComponenteDTO;
import cl.duoc.api_inventarios.Model.OrdenDTO;
//import cl.duoc.api_inventarios.Repository.AuditoriaRepository;
import cl.duoc.api_inventarios.Repository.ComponenteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j //log
public class ComponenteService {

    @Autowired
    private ComponenteRepository componenteRepository;

    //@Autowired
    //private AuditoriaRepository auditoriaRepository;

    RestTemplate restTemplate = new RestTemplate();

    private final String API_COMPRAS_URL = "http://localhost:28099/api/v1/proveedores/";

    public List<Componente> listarTodos() {
        log.debug("Consultando todos los componentes en la base de datos");
        return componenteRepository.findAll();
    }


    public Optional<Componente> buscarPorId(Long id) {
        log.debug("Buscando componente con ID: {} en la base de datos", id);
        return componenteRepository.findById(id);
    }



    public Componente ingresarComponente(Componente componente) {
        log.info("Iniciando validación para ingresar nuevo componente con fabricante_id: {}", componente.getFabricanteId());

        if (componenteRepository.existsByFabricanteId(componente.getFabricanteId())) {
            log.warn("Rechazado: Ya existe un componente con fabricante_id: {}", componente.getFabricanteId());
            throw new RuntimeException("Ya existe un componente con fabricante_id: " + componente.getFabricanteId());
        }

        Componente guardado = componenteRepository.save(componente);
        log.info("Componente guardado exitosamente en BD con el ID asignado: {}", guardado.getId());

        //registrarAuditoria(guardado.getId(), null, guardado.getEstado(), null, guardado.getUnidades());
        return guardado;
    }



    public ComponenteDTO consultarStock(Long id) {
        Componente c = componenteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fallo al consultar stock: No existe componente con ID: {}", id);
                    return new RuntimeException("Componente con id=" + id + " no encontrado");
                });

        List<OrdenDTO> ordenes = List.of();

        if (c.getUnidades() < 3) {
            log.warn("Stock crítico detectado ({} unidades) para componente ID: {}. Iniciando consulta a API externa...", c.getUnidades(), id);

            try {
                String urlExterna = API_COMPRAS_URL + "fabricante/" + c.getFabricanteId();
                log.info("Llamando a API Compras: {}", urlExterna);

                List<OrdenDTO> todas = restTemplate.exchange(
                        urlExterna,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<OrdenDTO>>() {}
                ).getBody();

                if (todas != null) {
                    ordenes = todas.stream()
                            .filter(o -> "EN_TRANSITO".equals(o.getEstado()) || "ADUANA".equals(o.getEstado()))
                            .toList();
                    log.info("Se encontraron {} órdenes en tránsito/aduana para el fabricante {}", ordenes.size(), c.getFabricanteId());
                }
            } catch (Exception e) {
                // Si la otra API está apagada, no queremos que nuestro sistema muera, solo registramos el error
                log.error("Error al comunicarse con la API de Compras: {}", e.getMessage());
            }
        } else {
            log.debug("Stock saludable ({} unidades) para componente ID: {}. No se requiere llamar a compras.", c.getUnidades(), id);
        }

        return new ComponenteDTO(c.getNombre(), c.getUnidades(), c.getEstado(), ordenes);
    }



    public Componente actualizarComponente(Long id, Componente componente) {
        log.info("Intentando actualizar componente ID: {}", id);

        return componenteRepository.findById(id)
                .map(c -> {
                    String estadoInicial = c.getEstado();
                    Integer unidadInicial = c.getUnidades();
                    componente.setId(id);
                    componenteRepository.save(componente);

                    log.info("Componente ID: {} actualizado. Estado cambió de '{}' a '{}'. Unidades cambiaron de {} a {}",
                            id, estadoInicial, componente.getEstado(), unidadInicial, componente.getUnidades());

                    //registrarAuditoria(id, estadoInicial, componente.getEstado(), unidadInicial, componente.getUnidades());
                    return componente;
                })
                .orElseThrow(() -> {
                    log.warn("Fallo al actualizar: Componente ID: {} no encontrado", id);
                    return new RuntimeException("Componente con id=" + id + " no encontrado");
                });
    }


    
    public void eliminarComponente(Long id) {
        log.info("Iniciando proceso de eliminación para componente ID: {}", id);

        componenteRepository.findById(id)
                .map(c -> {
                    if ("Agotado".equalsIgnoreCase(c.getEstado())) {
                        log.warn("Rechazado: Intento de eliminar componente '{}' (ID: {}) que está 'Agotado'. Regla de negocio ISO.", c.getNombre(), id);
                        throw new RuntimeException(
                                "El componente '" + c.getNombre() + "' está Agotado. " +
                                        "El registro contable está congelado para la revisión de fin de año (ISO)."
                        );
                    }

                    componenteRepository.deleteById(id);
                    log.info("Componente ID: {} eliminado exitosamente de la base de datos.", id);

                    /* registrarAuditoria(id, c.getEstado(), "ELIMINADO", c.getUnidades(), null); */
                    return c;
                })
                .orElseThrow(() -> {
                    log.warn("Fallo al eliminar: Componente ID: {} no encontrado", id);
                    return new RuntimeException("Componente con id=" + id + " no encontrado");
                });
    }

/*
    private void registrarAuditoria(Long componenteId, String estadoInicial,
                                    String estadoDespues, Integer unidadInicial,
                                    Integer unidadDespues) {
        auditoriaRepository.save(new Auditoria(componenteId, estadoInicial, estadoDespues, unidadInicial, unidadDespues));
    }*/
}