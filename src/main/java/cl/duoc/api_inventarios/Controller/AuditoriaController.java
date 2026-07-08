package cl.duoc.api_inventarios.Controller;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/auditorias")
@Tag(name = "Auditoria", description = "API para la gestión de registros de auditoría")
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    //ingresar un registro
    @PostMapping
    @Operation(summary = "Registrar una nueva acción", description = "Permite ingresar un nuevo registro de auditoría en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")})
    public ResponseEntity<Auditoria> ingresar(@Valid @RequestBody Auditoria auditoria){
        Auditoria nuevoRegistro = service.registrarAccion(auditoria);
        if (nuevoRegistro!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //listar registros
    @GetMapping
    @Operation(summary = "Listar registros de auditoría", description = "Retorna todos los registros de auditoría, sin filtros")
    @ApiResponse(responseCode = "200", description = "Lista de registros obtenida correctamente")
    public ResponseEntity<List<Auditoria>> listaRegistros() {
        return ResponseEntity.ok(service.listarRegistros());
    }

    //busqueda de registro por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro por ID", description = "Obtiene un registro de auditoría específico según su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    public ResponseEntity<Auditoria> listarUnRegistro(@PathVariable Long id){
        try {
            return ResponseEntity.ok(service.buscarRegistroPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //busqueda de registros por rango de fecha
    @GetMapping("/fecha")
    @Operation(summary = "Buscar registros por rango de fecha", description = "Obtiene los registros de auditoría entre dos fechas")
    @ApiResponse(responseCode = "200", description = "Registros encontrados")
    public ResponseEntity<List<Auditoria>> buscarPorRangoFecha(
            @Parameter(description = "Fecha de inicio", example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha de fin", example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<Auditoria> registros = service.filtrarRegistrosPorFecha(inicio, fin);
        return ResponseEntity.ok(registros);
    }

}