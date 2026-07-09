package cl.duoc.api_inventarios.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventarioTest {

    @Test
    void testCreacionYAtributosDeComponente() {
        // Arrange
        Componente componente = new Componente();
        componente.setId(1L);
        componente.setNombre("Tarjeta Gráfica RTX 4090");
        componente.setFabricanteId("FAB-001");
        componente.setUnidades(15);
        componente.setEstado("DISPONIBLE");

        // Act & Assert
        assertNotNull(componente);
        assertEquals(1L, componente.getId());
        assertEquals("Tarjeta Gráfica RTX 4090", componente.getNombre());
        assertEquals("FAB-001", componente.getFabricanteId());
        assertEquals(15, componente.getUnidades());
        assertEquals("DISPONIBLE", componente.getEstado());
    }
}
