package com.aldia.conveniopagos.repository.service.impl;

import com.aldia.conveniopagos.dto.ConvenioRequest;
import com.aldia.conveniopagos.dto.ConvenioResponse;
import com.aldia.conveniopagos.dto.PagoProgramadoDto;
import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.entity.Convenio;
import com.aldia.conveniopagos.entity.PromesaPago;
import com.aldia.conveniopagos.repository.service.ConvenioService;
import com.aldia.conveniopagos.repository.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConvenioServiceImpl implements ConvenioService {

    @Autowired
    private DataBaseService dataBaseService;

    @Override
    public List<PagoProgramadoDto> calcularPagosProgramados(ConvenioRequest convenioRequest) {
        double deudaRestante = convenioRequest.getMontoTotal() - convenioRequest.getMontoInicial();
        List<PagoProgramadoDto> pagosProgramados = new ArrayList<>();
        LocalDate fechaPago = convenioRequest.getFechaPrimerPago();
        pagosProgramados.add(new PagoProgramadoDto(fechaPago, convenioRequest.getMontoInicial()));
        DayOfWeek dia = convertirDiaSemana(convenioRequest.getDiaPago());
        fechaPago = ajustaDiaSeleccioando( fechaPago, dia );
        while (deudaRestante > 0) {
            double montoPago = Math.min(convenioRequest.getMontoPorPeriodo(), deudaRestante);
            fechaPago=ajustarFechaAlDiaPreferido(fechaPago, convenioRequest.getPeriodicidad());
            fechaPago=ajustaDiaSeleccioando( fechaPago, dia );
            pagosProgramados.add(new PagoProgramadoDto(fechaPago, montoPago));
            deudaRestante -= montoPago;
        }
        return pagosProgramados;
    }

    @Override
    public ConvenioResponse obtienePagosProgramados(ConvenioRequest convenioRequest) {
        Cliente cliente = dataBaseService.obtenerClientePorId(convenioRequest.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + convenioRequest.getIdCliente()));


        Convenio convenio = Convenio.builder()
                .cliente(cliente)
                .montoTotal(BigDecimal.valueOf(convenioRequest.getMontoTotal()))
                .montoInicial(BigDecimal.valueOf(convenioRequest.getMontoInicial()))
                .fechaPrimerPago(convenioRequest.getFechaPrimerPago())
                .diaPagoPreferido(convenioRequest.getDiaPago())
                .periodicidad(Convenio.Periodicidad.valueOf(convenioRequest.getPeriodicidad().toUpperCase()))
                .montoPorPeriodo(BigDecimal.valueOf(convenioRequest.getMontoPorPeriodo()))
                .build();
        dataBaseService.guardarConvenio(convenio);

        List<PagoProgramadoDto> pagosProgramados = calcularPagosProgramados(convenioRequest);

        for (PagoProgramadoDto pago : pagosProgramados) {
            PromesaPago promesaPago = PromesaPago.builder()
                    .convenio(convenio)
                    .fechaPago(pago.getFechaPago())
                    .monto(BigDecimal.valueOf(pago.getMonto()))
                    .estatus(PromesaPago.Estatus.PENDIENTE)
                    .build();
            dataBaseService.guardarPromesaPago(promesaPago);
        }



        ConvenioResponse response = ConvenioResponse.builder().idConvenio(1).pagosProgramados(pagosProgramados).build();
        return response;
    }

    private DayOfWeek convertirDiaSemana(String diaPagoPreferido) {
        return switch (diaPagoPreferido.toUpperCase()) {
            case "LUNES" -> DayOfWeek.MONDAY;
            case "MARTES" -> DayOfWeek.TUESDAY;
            case "MIÉRCOLES", "MIERCOLES" -> DayOfWeek.WEDNESDAY;
            case "JUEVES" -> DayOfWeek.THURSDAY;
            case "VIERNES" -> DayOfWeek.FRIDAY;
            case "SÁBADO", "SABADO" -> DayOfWeek.SATURDAY;
            case "DOMINGO" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Día de la semana no válido: " + diaPagoPreferido);
        };
    }

    private LocalDate ajustarFechaAlDiaPreferido(LocalDate fecha, String periodicidad) {
        switch (periodicidad.toUpperCase()) {
            case "SEMANAL" -> fecha = fecha.plusWeeks(1);
            case "QUINCENAL" -> fecha = fecha.plusWeeks(2);
            case "MENSUAL" -> fecha = fecha.plusMonths(1);
            default -> throw new IllegalArgumentException("Periodicidad no válida: " + periodicidad);
        }
        return fecha;
    }

    private LocalDate ajustaDiaSeleccioando(LocalDate fecha, DayOfWeek dia ) {
        while (fecha.getDayOfWeek() != dia) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }




}
