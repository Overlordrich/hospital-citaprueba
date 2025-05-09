package com.hospital.service;

import com.hospital.entity.Consultorio;
import com.hospital.repository.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

    // Obtener todos los consultorios
    public List<Consultorio> obtenerConsultorios() {
        return consultorioRepository.findAll();
    }

    // Obtener un consultorio por su ID
    public Consultorio obtenerConsultorio(Long id) {
        return consultorioRepository.findById(id).orElseThrow();
    }

    // Guardar un nuevo consultorio
    public void guardarConsultorio(Consultorio consultorio) {
        consultorioRepository.save(consultorio);
    }

    // Eliminar un consultorio por su ID
    public void eliminarConsultorio(Long id) {
        consultorioRepository.deleteById(id);
    }
}
