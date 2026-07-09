package cl.duoc.api_inventarios.Controller;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditoriaControllerTest {

    @Mock
    private AuditoriaService service;

    @InjectMocks
    private AuditoriaController controller;

    private Auditoria auditoria;

    @BeforeEach
    void setUp() {
        auditoria = new Auditoria(1L, "DISPONIBLE", "AGOTADO", 10, 0);
    }

    @Test
    void ingresarOk() {
        when(service.registrarAccion(auditoria)).thenReturn(auditoria);

        ResponseEntity<Auditoria> respuesta = controller.ingresar(auditoria);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(auditoria, respuesta.getBody());
        verify(service).registrarAccion(auditoria);
    }

    @Test
    void ingresar_bad() {
        when(service.registrarAccion(auditoria)).thenReturn(null);

        ResponseEntity<Auditoria> respuesta = controller.ingresar(auditoria);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    void listaRegistrosOk() {
        when(service.listarRegistros()).thenReturn(List.of(auditoria));

        ResponseEntity<List<Auditoria>> respuesta = controller.listaRegistros();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(service).listarRegistros();
    }

    @Test
    void listarUnRegistroOk() {
        when(service.buscarRegistroPorId(1L)).thenReturn(auditoria);

        ResponseEntity<Auditoria> respuesta = controller.listarUnRegistro(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(auditoria, respuesta.getBody());
    }

    @Test
    void listarUnRegistro_bad() {
        when(service.buscarRegistroPorId(99L)).thenThrow(new RuntimeException("Registro no encontrado: 99"));

        ResponseEntity<Auditoria> respuesta = controller.listarUnRegistro(99L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    void buscarPorRangoFechaOk() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        when(service.filtrarRegistrosPorFecha(inicio, fin)).thenReturn(List.of(auditoria));

        ResponseEntity<List<Auditoria>> respuesta = controller.buscarPorRangoFecha(inicio, fin);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(service).filtrarRegistrosPorFecha(inicio, fin);
    }

    @Test
    void buscarPorRangoFecha_Bad() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        when(service.filtrarRegistrosPorFecha(inicio, fin)).thenReturn(List.of());

        ResponseEntity<List<Auditoria>> respuesta = controller.buscarPorRangoFecha(inicio, fin);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
    }
}