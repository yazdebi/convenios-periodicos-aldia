package com.aldia.conveniopagos.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@ToString
public class ConvenioResponse {

    int idConvenio;
    List<PagoProgramadoDto> pagosProgramados;
}
