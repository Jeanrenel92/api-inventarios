package cl.duoc.api_inventarios.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "COMPONENTES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "El fabricante_id no puede estar vacío")
    @Column(name = "ID_FABRICANTE", nullable = false, unique = true)
    private String fabricanteId;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio no acepta valores negativos o cero")
    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "Las unidades no pueden estar vacías")
    @PositiveOrZero(message = "Las unidades no aceptan valores negativos")
    @Column(name = "UNIDADES", nullable = false)
    private Integer unidades;

    @NotBlank(message = "El estado debe ser: En Stock, Asignado, Agotado")
    @Pattern(regexp = "En_Stock|Asignado|Agotado",
            message = "El estado debe ser: En Stock, Asignado, Agotado")
    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @NotNull(message = "El bodega_id no puede estar vacío")
    @Column(name = "ID_BODEGA", nullable = false)
    private Long idBodega;
}