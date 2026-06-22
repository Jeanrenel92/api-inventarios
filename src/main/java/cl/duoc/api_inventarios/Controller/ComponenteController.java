package cl.duoc.api_inventarios.Controller;


import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Model.ComponenteDTO;
import cl.duoc.api_inventarios.Service.ComponenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventarios")
public class ComponenteController {

    @Autowired
    private ComponenteService service;

    @PostMapping
    @Operation(summary = "Registrar una nueva orden", description = "Permite ingresar una nueva orden en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")})
    public ResponseEntity<Componente> ingresar(@Valid @RequestBody Componente componente) {
        Componente compNuevo = service.ingresarComponente(componente);
        if (compNuevo != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(compNuevo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    public ResponseEntity<List<Componente>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Componente> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<ComponenteDTO> consultarStock(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(c -> ResponseEntity.ok(service.consultarStock(id)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Componente> actualizar(@PathVariable Long id, @Valid @RequestBody Componente componente) {
        try {
            return ResponseEntity.ok(service.actualizarComponente(id, componente));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminarComponente(id);
            return ResponseEntity.ok("Componente con id=" + id + " eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
