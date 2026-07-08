package cl.duoc.api_inventarios.Model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaTest {

    @Test
    void testConstructorVacioYSetters() {
        Auditoria auditoria = new Auditoria();

        auditoria.setId(1L);
        auditoria.setComponenteId(100L);
        auditoria.setEstadoInicial("Disponible");
        auditoria.setEstadoDespues("Agotado");
        auditoria.setUnidadInicial(10);
        auditoria.setUnidadDespues(0);

        LocalDateTime fecha = LocalDateTime.now();
        auditoria.setFecha(fecha);

        assertEquals(1L, auditoria.getId());
        assertEquals(100L, auditoria.getComponenteId());
        assertEquals("Disponible", auditoria.getEstadoInicial());
        assertEquals("Agotado", auditoria.getEstadoDespues());
        assertEquals(10, auditoria.getUnidadInicial());
        assertEquals(0, auditoria.getUnidadDespues());
        assertEquals(fecha, auditoria.getFecha());
    }

    @Test
    void testConstructorPersonalizado() {
        Auditoria auditoria = new Auditoria(
                100L,
                "Disponible",
                "Agotado",
                10,
                0
        );

        assertEquals(100L, auditoria.getComponenteId());
        assertEquals("Disponible", auditoria.getEstadoInicial());
        assertEquals("Agotado", auditoria.getEstadoDespues());
        assertEquals(10, auditoria.getUnidadInicial());
        assertEquals(0, auditoria.getUnidadDespues());
    }

    @Test
    void testOnCreateAsignaFecha() {
        Auditoria auditoria = new Auditoria();

        assertNull(auditoria.getFecha());

        auditoria.onCreate();

        assertNotNull(auditoria.getFecha());
    }

    @Test
    void testConstructorCompleto() {
        LocalDateTime fecha = LocalDateTime.now();

        Auditoria auditoria = new Auditoria(
                1L,
                100L,
                "Disponible",
                "Agotado",
                10,
                0,
                fecha
        );

        assertEquals(1L, auditoria.getId());
        assertEquals(100L, auditoria.getComponenteId());
        assertEquals("Disponible", auditoria.getEstadoInicial());
        assertEquals("Agotado", auditoria.getEstadoDespues());
        assertEquals(10, auditoria.getUnidadInicial());
        assertEquals(0, auditoria.getUnidadDespues());
        assertEquals(fecha, auditoria.getFecha());
    }
}