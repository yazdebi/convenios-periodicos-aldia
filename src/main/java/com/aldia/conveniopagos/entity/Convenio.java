package com.aldia.conveniopagos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "convenios", schema = "ALDIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Convenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_convenio")
    private Integer idConvenio;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "monto_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "fecha_primer_pago", nullable = false)
    private LocalDate fechaPrimerPago;

    @Column(name = "dia_pago_preferido", nullable = false, length = 10)
    private String diaPagoPreferido;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidad", nullable = false)
    private Periodicidad periodicidad;

    @Column(name = "monto_por_periodo", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPorPeriodo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public enum Periodicidad {
        SEMANAL,
        QUINCENAL,
        MENSUAL
    }
}