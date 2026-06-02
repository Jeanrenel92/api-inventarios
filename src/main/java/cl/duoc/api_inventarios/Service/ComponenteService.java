package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Model.ComponenteDTO;
import cl.duoc.api_inventarios.Model.OrdenDTO;
import cl.duoc.api_inventarios.Repository.AuditoriaRepository;
import cl.duoc.api_inventarios.Repository.ComponenteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ComponenteService {

    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    RestTemplate restTemplate = new RestTemplate();

    private final String API_COMPRAS_URL = "http://localhost:28099/api/v1/proveedores/";

    public List<Componente> listarTodos() {
        return componenteRepository.findAll();
    }

    public Optional<Componente> buscarPorId(Long id) {
        return componenteRepository.findById(id);
    }

    public Componente ingresarComponente(Componente componente) {
        if (componenteRepository.existsByFabricanteId(componente.getFabricanteId())) {
            throw new RuntimeException("Ya existe un componente con fabricante_id: " + componente.getFabricanteId());
        }
        Componente guardado = componenteRepository.save(componente);
        registrarAuditoria(guardado.getId(), null, guardado.getEstado(), null, guardado.getUnidades());
        return guardado;
    }

    public ComponenteDTO consultarStock(Long id) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Componente con id=" + id + " no encontrado"));

        List<OrdenDTO> ordenes = List.of();

        if (componente.getUnidades() < 3) {
            List<OrdenDTO> listaOrdenes = restTemplate.exchange(
                    API_COMPRAS_URL + "fabricante/" + componente.getFabricanteId(),
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<OrdenDTO>>() {}
            ).getBody();

            if (listaOrdenes != null) {
                ordenes = listaOrdenes.stream()
                        .filter(o -> "EN_TRANSITO".equals(o.getEstado())
                                || "ADUANA".equals(o.getEstado()))
                        .toList();
            }
        }

        return new ComponenteDTO(componente.getNombre(), componente.getUnidades(), componente.getEstado(), ordenes);
    }

    public Componente actualizarComponente(Long id, Componente componente) {
        return componenteRepository.findById(id)
                .map(c -> {
                    String estadoInicial = c.getEstado();
                    Integer unidadInicial = c.getUnidades();
                    componente.setId(id);
                    componenteRepository.save(componente);
                    registrarAuditoria(id, estadoInicial, componente.getEstado(), unidadInicial, componente.getUnidades());
                    return componente;
                })
                .orElseThrow(() -> new RuntimeException("Componente con id=" + id + " no encontrado"));
    }

    public void eliminarComponente(Long id) {
        componenteRepository.findById(id)
                .map(c -> {
                    if ("Agotado".equalsIgnoreCase(c.getEstado())) {
                        throw new RuntimeException(
                                "El componente '" + c.getNombre() + "' está Agotado. " +
                                        "El registro contable está congelado para la revisión de fin de año (ISO)."
                        );
                    }
                    registrarAuditoria(id, c.getEstado(), "ELIMINADO", c.getUnidades(), null);
                    componenteRepository.deleteById(id);
                    return c;
                })
                .orElseThrow(() -> new RuntimeException("Componente con id=" + id + " no encontrado"));
    }

    private void registrarAuditoria(Long componenteId, String estadoInicial,
                                    String estadoDespues, Integer unidadInicial,
                                    Integer unidadDespues) {
        auditoriaRepository.save(new Auditoria(componenteId, estadoInicial, estadoDespues, unidadInicial, unidadDespues));
    }
}