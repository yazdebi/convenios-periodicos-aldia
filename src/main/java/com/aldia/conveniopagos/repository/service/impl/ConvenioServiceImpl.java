package com.aldia.conveniopagos.repository.service.impl;

import com.aldia.conveniopagos.dto.ConvenioRequest;
import com.aldia.conveniopagos.dto.ConvenioResponse;
import com.aldia.conveniopagos.dto.PagoProgramadoDto;
import com.aldia.conveniopagos.repository.service.ConvenioService;
import com.aldia.conveniopagos.repository.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // Agregar el monto inicial como un pago independiente
        LocalDate fechaPago = convenioRequest.getFechaPrimerPago();
        pagosProgramados.add(new PagoProgramadoDto(fechaPago, convenioRequest.getMontoInicial()));

        // Ajustar la fecha al día de pago preferido asegura que sea el dia que el cliente desea
        DayOfWeek diaPreferido = convertirDiaSemana(convenioRequest.getDiaPago().toUpperCase());
        while (fechaPago.getDayOfWeek() != diaPreferido) {
            fechaPago = fechaPago.plusDays(1);
        }

        // Agregar el primer pago basado en el montoPorPeriodo
        double montoPrimerPago = Math.min(deudaRestante, convenioRequest.getMontoPorPeriodo());
        pagosProgramados.add(new PagoProgramadoDto(fechaPago, montoPrimerPago));
        deudaRestante -= montoPrimerPago;

        // Calcular la periodicidad en días
        int diasPeriodicidad = switch (convenioRequest.getPeriodicidad().toUpperCase()) {
            case "SEMANAL" -> 7;
            case "QUINCENAL" -> 14;
            case "MENSUAL" -> 30;
            default -> throw new IllegalArgumentException("Periodicidad no válida");
        };

        // Calcular los pagos restantes
        while (deudaRestante > 0) {
            fechaPago = fechaPago.plusDays(diasPeriodicidad);
            double montoPago = Math.min(deudaRestante, convenioRequest.getMontoPorPeriodo());
            pagosProgramados.add(new PagoProgramadoDto(fechaPago, montoPago));
            deudaRestante -= montoPago;
        }

        return pagosProgramados;
    }

    @Override
    public ConvenioResponse obtienePagosProgramados(ConvenioRequest convenioRequest) {
        ConvenioResponse response = ConvenioResponse.builder().idConvenio(1).pagosProgramados(calcularPagosProgramados(convenioRequest)).build();
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




}
