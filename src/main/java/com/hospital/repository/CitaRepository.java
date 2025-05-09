package com.hospital.repository;

import com.hospital.entity.Cita;
import com.hospital.entity.Consultorio;
import com.hospital.entity.Medico;
import com.hospital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    boolean existsByConsultorioAndHorario(Consultorio consultorio, LocalDateTime horario);

    boolean existsByDoctorAndHorario(Medico doctor, LocalDateTime horario);

    List<Cita> findByPacienteAndHorarioBetween(Paciente paciente, LocalDateTime start, LocalDateTime end);

    List<Cita> findByDoctorAndHorarioBetween(Medico doctor, LocalDateTime start, LocalDateTime end);

    // Buscar citas por consultorio y fecha
    List<Cita> findByConsultorioAndHorarioBetween(Consultorio consultorio, LocalDateTime start, LocalDateTime end);

    // Buscar citas por fecha (sin considerar doctor ni consultorio)
    List<Cita> findByHorarioBetween(LocalDateTime start, LocalDateTime end);

}

