package cl.duoc.api_inventarios.Controller;

import cl.duoc.api_inventarios.Model.Componente;
import cl.duoc.api_inventarios.Service.ComponenteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Carga solo la capa Web (Controlador) para mas rapidez
@WebMvcTest(ComponenteController.class)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComponenteService componenteService;

    @Test
    void buscarPorId_DeberiaRetornar200() throws Exception {
        Componente componente = new Componente();
        componente.setId(1L);
        componente.setNombre("Teclado Mecánico");

        when(componenteService.buscarPorId(1L)).thenReturn(Optional.of(componente));

        // Act & Assert
        mockMvc.perform(get("/api/v1/inventarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos HTTP 200 ok
                .andExpect(jsonPath("$.nombre").value("Teclado Mecánico")); // Revisamos el JSON
    }

    @Test
    void buscarPorId_NoEncontrado_DeberiaRetornar404() throws Exception {
        when(componenteService.buscarPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert ejecutar y verificar
        mockMvc.perform(get("/api/v1/inventarios/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Esperamos HTTP 404 NotFound
    }
}