package com.aldia.conveniopagos.repository;

import com.aldia.conveniopagos.entity.PromesaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromesaPagoRepository extends JpaRepository<PromesaPago, Integer> {
}