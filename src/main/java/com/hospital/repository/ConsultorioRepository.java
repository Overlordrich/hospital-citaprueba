package com.hospital.repository;

import com.hospital.entity.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio,Long> {
    Consultorio findByNumeroConsultorio(String numeroConsultorio);
}
