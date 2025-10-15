package com.aldia.conveniopagos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promesas_pago", schema = "ALDIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromesaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promesa")
    private Integer idPromesa;

    @ManyToOne
    @JoinColumn(name = "id_convenio", nullable = false)
    private Convenio convenio;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false)
    private Estatus estatus = Estatus.PENDIENTE;

    public enum Estatus {
        PENDIENTE,
        PAGADO
    }
}
