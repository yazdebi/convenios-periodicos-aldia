package com.aldia.conveniopagos.repository.service.impl;

import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.repository.ClienteRepository;
import com.aldia.conveniopagos.repository.service.ConvenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ConvenioServiceImpl implements ConvenioService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    public Optional<Cliente> obtenerClientePorId(Integer idCliente) {
          return clienteRepository.findById(idCliente);
    }
}
