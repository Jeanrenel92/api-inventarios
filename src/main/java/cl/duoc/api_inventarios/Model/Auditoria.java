package cl.duoc.api_inventarios.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPONENTE_ID", nullable = false)
    private Long componenteId;

    @Column(name = "ESTADO_INICIAL")
    private String estadoInicial;

    @Column(name = "ESTADO_DESPUES", nullable = false)
    private String estadoDespues;

    @Column(name = "UNIDADES_INICIAL")
    private Integer unidadInicial;

    @Column(name = "UNIDADES_DESPUES")
    private Integer unidadDespues;

    @Column(name = "FECHA_REGISTRO")
    private LocalDateTime fecha ;

    public Auditoria(Long componenteId, String estadoInicial, String estadoDespues, Integer unidadInicial, Integer unidadDespues) {
        this.componenteId = componenteId;
        this.estadoInicial = estadoInicial;
        this.estadoDespues = estadoDespues;
        this.unidadInicial = unidadInicial;
        this.unidadDespues = unidadDespues;
    }

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }


}
