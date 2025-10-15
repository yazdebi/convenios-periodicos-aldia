package com.aldia.conveniopagos.repository.service;

import com.aldia.conveniopagos.entity.Cliente;

import java.util.Optional;

public interface DataBaseService {
    public Optional<Cliente> obtenerClientePorId(Integer idCliente);
}
