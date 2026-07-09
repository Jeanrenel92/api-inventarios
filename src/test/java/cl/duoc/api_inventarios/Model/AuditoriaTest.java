package cl.duoc.api_inventarios.Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaTest {

    private Auditoria auditoriaValida() {
        return new Auditoria(1L, "DISPONIBLE", "AGOTADO", 10, 0);
    }

    @Test
    void VerificarCampos() {
        Auditoria auditoria = auditoriaValida();

        assertEquals(1L, auditoria.getComponenteId());
        assertEquals("DISPONIBLE", auditoria.getEstadoInicial());
        assertEquals("AGOTADO", auditoria.getEstadoDespues());
        assertEquals(10, auditoria.getUnidadInicial());
        assertEquals(0, auditoria.getUnidadDespues());
    }

    @Test
    void sinIdYFecha() {
        Auditoria auditoria = auditoriaValida();

        assertNull(auditoria.getId());
        assertNull(auditoria.getFecha());
    }

    @Test
    void onCreate_asignaFechaAutomatico() {
        Auditoria auditoria = auditoriaValida();

        auditoria.onCreate();

        assertNotNull(auditoria.getFecha());
        assertTrue(auditoria.getFecha().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void gettersYSetters_OK() {
        Auditoria auditoria = new Auditoria();
        LocalDateTime fecha = LocalDateTime.now();

        auditoria.setId(1L);
        auditoria.setComponenteId(2L);
        auditoria.setEstadoInicial("ACTIVO");
        auditoria.setEstadoDespues("INACTIVO");
        auditoria.setUnidadInicial(5);
        auditoria.setUnidadDespues(3);
        auditoria.setFecha(fecha);

        assertEquals(1L, auditoria.getId());
        assertEquals(2L, auditoria.getComponenteId());
        assertEquals("ACTIVO", auditoria.getEstadoInicial());
        assertEquals("INACTIVO", auditoria.getEstadoDespues());
        assertEquals(5, auditoria.getUnidadInicial());
        assertEquals(3, auditoria.getUnidadDespues());
        assertEquals(fecha, auditoria.getFecha());
    }

    @Test
    void constructorVacio() {
        Auditoria auditoria = new Auditoria();

        assertNull(auditoria.getId());
        assertNull(auditoria.getComponenteId());
        assertNull(auditoria.getEstadoInicial());
        assertNull(auditoria.getEstadoDespues());
    }

    @Test
    void allArgsConstructor() {
        LocalDateTime fecha = LocalDateTime.now();
        Auditoria auditoria = new Auditoria(1L, 2L, "A", "B", 1, 2, fecha);

        assertEquals(1L, auditoria.getId());
        assertEquals(2L, auditoria.getComponenteId());
        assertEquals("A", auditoria.getEstadoInicial());
        assertEquals("B", auditoria.getEstadoDespues());
        assertEquals(1, auditoria.getUnidadInicial());
        assertEquals(2, auditoria.getUnidadDespues());
        assertEquals(fecha, auditoria.getFecha());
    }
}