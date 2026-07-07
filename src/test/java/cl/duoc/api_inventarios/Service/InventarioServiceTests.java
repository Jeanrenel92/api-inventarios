package cl.duoc.api_inventarios.Service;

import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Repository.ComponenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTests {

    @Mock // Simulamos el repositorio (BD)
    private ComponenteRepository componenteRepository;

    @InjectMocks
    private ComponenteService componenteService;

    @Test
    void buscarPorId_Exito() {
        // Arrange
        Componente mockComponente = new Componente();
        mockComponente.setId(100L);
        mockComponente.setNombre("Procesador i9");

        when(componenteRepository.findById(100L)).thenReturn(Optional.of(mockComponente));

        // Act
        Optional<Componente> resultado = componenteService.buscarPorId(100L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Procesador i9", resultado.get().getNombre());
        verify(componenteRepository, times(1)).findById(100L); // Verifica que se llamó a la BD 1 vez
    }

    @Test
    void ingresarComponente_FallaPorFabricanteDuplicado() {
        // Arrange
        Componente nuevo = new Componente();
        nuevo.setFabricanteId("DUPLICADO-123");

        // Simulamos que la BD ya encontró ese fabricante
        when(componenteRepository.existsByFabricanteId("DUPLICADO-123")).thenReturn(true);

        // Act & Assert
        // Comprobamos que el servicio "explota" con la excepción correcta de negocio
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            componenteService.ingresarComponente(nuevo);
        });

        assertTrue(excepcion.getMessage().contains("Ya existe un componente con fabricante_id"));

        // Verificamos que JAMÁS se intentó guardar en la BD
        verify(componenteRepository, never()).save(any(Componente.class));
    }
}