package com.aldia.conveniopagos.repository;

import com.aldia.conveniopagos.entity.Convenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Integer> {

}
