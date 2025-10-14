package com.aldia.conveniopagos.controller;

import com.aldia.conveniopagos.dto.ConvenioRequest;
import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.repository.service.ConvenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ControllerConvenioPagos {
    @Autowired
    private ConvenioService convenioService;
    @PostMapping("/convenio-pagos")
    public ResponseEntity<String> crearConvenio(@RequestBody ConvenioRequest dto) {
        // Lógica para manejar el DTO
        String respuesta = "Convenio creado para el cliente con ID: " +dto.getIdCliente();
        /*+
                ", monto total: " + dto.getMontoTotal() +
                ", fecha del primer pago: " + dto.getFechaPrimerPago() +
                ", día de pago: " + dto.getDiaPago() +
                ", periodicidad: " + dto.getPeriodicidad() +
                ", monto por periodo: " + dto.getMontoPorPeriodo();*/
        Optional<Cliente> cliente = convenioService.obtenerClientePorId(1);
        System.out.printf("Cliente: %s", cliente.get().getNombre());
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

}
