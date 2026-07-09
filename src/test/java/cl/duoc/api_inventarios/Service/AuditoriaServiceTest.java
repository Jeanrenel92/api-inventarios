package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Auditoria;
import cl.duoc.api_inventarios.Repository.AuditoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private AuditoriaRepository repository;

    @InjectMocks
    private AuditoriaService service;

    private Auditoria auditoriaValida() {
        return new Auditoria(1L, "DISPONIBLE", "AGOTADO", 10, 0);
    }

    @Test
    void listarRegistrosOk() {
        List<Auditoria> lista = List.of(auditoriaValida());
        when(repository.findAll()).thenReturn(lista);

        List<Auditoria> resultado = service.listarRegistros();

        assertEquals(lista, resultado);
        verify(repository).findAll();
    }

    @Test
    void listarRegistrosVacio() {
        when(repository.findAll()).thenReturn(List.of());

        List<Auditoria> resultado = service.listarRegistros();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarporIdOk() {
        Auditoria auditoria = auditoriaValida();
        when(repository.findById(1L)).thenReturn(Optional.of(auditoria));

        Auditoria resultado = service.buscarRegistroPorId(1L);

        assertEquals(auditoria, resultado);
    }

    @Test
    void buscarporIdSinResultado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.buscarRegistroPorId(99L));

        assertEquals("Registro no encontrado: 99", ex.getMessage());
    }

    @Test
    void registrarAccionOk() {
        Auditoria auditoria = auditoriaValida();
        when(repository.save(auditoria)).thenReturn(auditoria);

        Auditoria resultado = service.registrarAccion(auditoria);

        assertEquals(auditoria, resultado);
        verify(repository).save(auditoria);
    }

    @Test
    void filtrarPorFechaOk() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.atTime(23, 59, 59, 999_999_999);
        List<Auditoria> lista = List.of(auditoriaValida());

        when(repository.findByFechaBetween(desde, hasta)).thenReturn(lista);

        List<Auditoria> resultado = service.filtrarRegistrosPorFecha(inicio, fin);

        assertEquals(lista, resultado);
        verify(repository).findByFechaBetween(eq(desde), eq(hasta));
    }

    @Test
    void filtrarPorFechaBad() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);

        when(repository.findByFechaBetween(any(), any())).thenReturn(List.of());

        List<Auditoria> resultado = service.filtrarRegistrosPorFecha(inicio, fin);

        assertTrue(resultado.isEmpty());
    }
}