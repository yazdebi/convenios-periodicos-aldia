package com.aldia.conveniopagos.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PagoProgramadoDto {
    private LocalDate fechaPago;
    private double monto;
}
