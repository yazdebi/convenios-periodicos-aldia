package com.aldia.conveniopagos.repository.service.impl;

import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.entity.Convenio;
import com.aldia.conveniopagos.entity.PromesaPago;
import com.aldia.conveniopagos.repository.ClienteRepository;
import com.aldia.conveniopagos.repository.ConvenioRepository;
import com.aldia.conveniopagos.repository.PromesaPagoRepository;
import com.aldia.conveniopagos.repository.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataBaseServiceImpl implements DataBaseService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ConvenioRepository convenioRepository;
    @Autowired
    private PromesaPagoRepository promesaPagoRepository;
    @Override
    public Optional<Cliente> obtenerClientePorId(Integer idCliente) {
        return clienteRepository.findById(idCliente);
    }

    @Override
    public Convenio guardarConvenio(Convenio convenio) {
        return convenioRepository.save(convenio);
    }

    @Override
    public PromesaPago guardarPromesaPago(PromesaPago promesaPago) {
        return promesaPagoRepository.save(promesaPago);
    }


}
