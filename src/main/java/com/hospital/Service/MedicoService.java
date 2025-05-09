package com.hospital.service;

import com.hospital.entity.Medico;
import com.hospital.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    // Obtener todos los médicos
    public List<Medico> obtenerMedicos() {
        return medicoRepository.findAll();
    }

    // Obtener un médico por su ID
    public Medico obtenerMedico(Long id) {
        return medicoRepository.findById(id).orElseThrow();
    }

    // Guardar un nuevo médico
    public void guardarMedico(Medico medico) {
        medicoRepository.save(medico);
    }

    // Eliminar un médico por su ID
    public void eliminarMedico(Long id) {
        medicoRepository.deleteById(id);
    }
}
