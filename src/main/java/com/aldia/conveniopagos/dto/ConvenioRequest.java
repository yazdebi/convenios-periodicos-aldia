package com.aldia.conveniopagos.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConvenioRequest {
    private int idCliente;
    private double montoTotal;
    private double montoInicial;
    private LocalDate fechaPrimerPago;
    private String diaPago;
    private String periodicidad;
    private double montoPorPeriodo;
}
