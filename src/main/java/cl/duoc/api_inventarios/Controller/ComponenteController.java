package cl.duoc.api_inventarios.Controller;

import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Model.ComponenteDTO;
import cl.duoc.api_inventarios.Service.ComponenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j; // <-- Importación obligatoria de Lombok
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventarios")
@Tag(name = "Componentes", description = "API para la gestión del inventario de componentes")
@Slf4j //'log'
public class ComponenteController {

    @Autowired
    private ComponenteService service;

    @PostMapping
    @Operation(summary = "Registrar un nuevo componente", description = "Permite ingresar un nuevo componente al inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Componente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados en la petición")
    })
    public ResponseEntity<Componente> ingresar(@Valid @RequestBody Componente componente) {
        log.info("Petición REST recibida: Registrar nuevo componente");

        Componente compNuevo = service.ingresarComponente(componente);
        if (compNuevo != null) {
            log.debug("Componente registrado y respondiendo con HTTP 201");
            return ResponseEntity.status(HttpStatus.CREATED).body(compNuevo);
        }

        log.warn("La creación del componente falló, respondiendo con HTTP 400");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    @Operation(summary = "Listar todos los componentes", description = "Obtiene una lista completa de los componentes en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public ResponseEntity<List<Componente>> listarTodos() {
        log.info("Petición REST recibida: Listar todos los componentes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar componente por ID", description = "Obtiene los detalles de un componente específico según su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Componente encontrado"),
            @ApiResponse(responseCode = "404", description = "El componente no existe en la base de datos")
    })
    public ResponseEntity<Componente> buscarPorId(
            @Parameter(description = "ID único del componente", example = "1") @PathVariable Long id) {
        log.info("Petición REST recibida: Buscar componente por ID: {}", id);

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.info("Componente ID: {} no encontrado, devolviendo HTTP 404", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/stock/{id}")
    @Operation(summary = "Consultar stock", description = "Obtiene únicamente la información de stock (DTO) de un componente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock consultado correctamente"),
            @ApiResponse(responseCode = "404", description = "El componente no existe")
    })
    public ResponseEntity<ComponenteDTO> consultarStock(
            @Parameter(description = "ID del componente para consultar stock", example = "1") @PathVariable Long id) {
        log.info("Petición REST recibida: Consultar stock del componente ID: {}", id);

        return service.buscarPorId(id)
                .map(c -> ResponseEntity.ok(service.consultarStock(id)))
                .orElseGet(() -> {
                    log.info("No se pudo consultar stock. Componente ID: {} no encontrado", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un componente", description = "Sobrescribe la información de un componente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Componente actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Componente no encontrado")
    })
    public ResponseEntity<Componente> actualizar(
            @Parameter(description = "ID del componente a actualizar") @PathVariable Long id,
            @Valid @RequestBody Componente componente) {
        log.info("Petición REST recibida: Actualizar componente ID: {}", id);

        try {
            return ResponseEntity.ok(service.actualizarComponente(id, componente));
        } catch (RuntimeException e) {
            log.warn("Fallo al actualizar el componente ID: {}. Motivo: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un componente", description = "Elimina un componente del inventario permanentemente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Componente eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Operación no permitida (ej. no se puede eliminar)"),
            @ApiResponse(responseCode = "404", description = "Componente no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del componente a eliminar") @PathVariable Long id) {
        log.info("Petición REST recibida: Eliminar componente ID: {}", id);

        try {
            service.eliminarComponente(id);
            log.info("Componente ID: {} eliminado correctamente", id); // LOG: Éxito
            return ResponseEntity.ok("Componente con id=" + id + " eliminado exitosamente");
        } catch (RuntimeException e) {
            log.warn("Fallo al eliminar el componente ID: {}. Motivo: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}