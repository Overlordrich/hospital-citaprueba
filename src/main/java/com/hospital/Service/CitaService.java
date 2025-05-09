package com.hospital.service;

import com.hospital.entity.Cita;
import com.hospital.entity.Consultorio;
import com.hospital.entity.Medico;
import com.hospital.entity.Paciente;
import com.hospital.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;

    // Validar que la cita cumpla con las reglas de negocio
    public boolean validarCita(Cita nuevaCita, Paciente paciente, Medico doctor, Consultorio consultorio) {
        LocalDateTime horario = nuevaCita.getHorario();
        LocalDateTime inicioDia = horario.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        // 1. No puede haber cita en el mismo consultorio a la misma hora
        boolean conflictoConsultorio = citaRepository.existsByConsultorioAndHorario(consultorio, horario);

        // 2. No puede haber cita para el mismo doctor a la misma hora
        boolean conflictoDoctor = citaRepository.existsByDoctorAndHorario(doctor, horario);

        // 3. Paciente no puede tener dos citas a menos de 2 horas
        List<Cita> citasPaciente = citaRepository.findByPacienteAndHorarioBetween(paciente, inicioDia, finDia);
        boolean conflictoPaciente = citasPaciente.stream()
                .anyMatch(cita -> Math.abs(cita.getHorario().getHour() - horario.getHour()) < 2);

        // 4. El doctor no puede tener más de 8 citas en el día
        List<Cita> citasDoctor = citaRepository.findByDoctorAndHorarioBetween(doctor, inicioDia, finDia);
        boolean excesoDoctor = citasDoctor.size() >= 8;

        return !(conflictoConsultorio || conflictoDoctor || conflictoPaciente || excesoDoctor);
    }

    // Guardar una nueva cita
    public void guardarCita(Cita cita) {
        citaRepository.save(cita);
    }

    // Buscar todas las citas
    public List<Cita> obtenerCitas() {
        return citaRepository.findAll();
    }

    // Buscar una cita por su ID
    public Cita obtenerCita(Long id) {
        return citaRepository.findById(id).orElseThrow();
    }

    // Eliminar una cita por su ID
    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }
}
