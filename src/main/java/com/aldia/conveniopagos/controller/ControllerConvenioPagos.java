package com.aldia.conveniopagos.controller;

import com.aldia.conveniopagos.dto.ConvenioRequest;
import com.aldia.conveniopagos.dto.ConvenioResponse;
import com.aldia.conveniopagos.dto.PagoProgramadoDto;
import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.repository.service.ConvenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ControllerConvenioPagos {
    @Autowired
    private ConvenioService convenioService;
    @PostMapping("/convenio-pagos")
    public ResponseEntity<ConvenioResponse> crearConvenio(@RequestBody ConvenioRequest dto) {
        ConvenioResponse response = convenioService.obtienePagosProgramados(dto);
        System.out.printf("Response: %s", response.toString());
        //String respuesta = "Convenio creado para el cliente con ID: " +dto.getIdCliente();

       return new ResponseEntity<ConvenioResponse>(response, HttpStatus.CREATED);
    }

}
