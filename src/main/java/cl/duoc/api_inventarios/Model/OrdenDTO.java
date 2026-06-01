package cl.duoc.api_inventarios.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {

    private String idFabricante;
    private Integer unidad;
    private String estado;
    private String nomProveedor;
    private Date fechaOrden;


}
