package cl.duoc.api_inventarios.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponenteDTO {

    private String nombre;
    private Integer unidades;
    private String estado;
    private List<OrdenDTO> ordenesEnTransito;
}
