package com.hospital.service;

import com.hospital.entity.Paciente;
import com.hospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Obtener todos los pacientes
    public List<Paciente> obtenerPacientes() {
        return pacienteRepository.findAll();
    }

    // Obtener un paciente por su ID
    public Paciente obtenerPaciente(Long id) {
        return pacienteRepository.findById(id).orElseThrow();
    }

    // Guardar un nuevo paciente
    public void guardarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    // Eliminar un paciente por su ID
    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
}
