package com.aldia.conveniopagos.repository.service;

import com.aldia.conveniopagos.entity.Cliente;
import com.aldia.conveniopagos.entity.Convenio;
import com.aldia.conveniopagos.entity.PromesaPago;

import java.util.Optional;

public interface DataBaseService {
    public Optional<Cliente> obtenerClientePorId(Integer idCliente);
    public Convenio guardarConvenio(Convenio convenio);

    public PromesaPago guardarPromesaPago(PromesaPago promesaPago);

}
