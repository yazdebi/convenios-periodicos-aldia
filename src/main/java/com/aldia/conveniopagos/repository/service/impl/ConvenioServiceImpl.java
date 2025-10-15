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



        ConvenioResponse response = ConvenioResponse.builder().idConvenio(convenio.getIdConvenio()).pagosProgramados(pagosProgramados).build();
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
