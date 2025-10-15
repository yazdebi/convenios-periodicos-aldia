package com.aldia.conveniopagos.repository.service;

import com.aldia.conveniopagos.dto.ConvenioRequest;
import com.aldia.conveniopagos.dto.ConvenioResponse;
import com.aldia.conveniopagos.dto.PagoProgramadoDto;
import com.aldia.conveniopagos.entity.Cliente;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConvenioService {



    List<PagoProgramadoDto> calcularPagosProgramados(ConvenioRequest convenioRequest);

    ConvenioResponse obtienePagosProgramados(ConvenioRequest convenioRequest);
}
