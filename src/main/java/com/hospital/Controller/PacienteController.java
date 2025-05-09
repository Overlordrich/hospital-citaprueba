package com.hospital.Controller;

import com.hospital.entity.Paciente;
import com.hospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    @GetMapping
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "pacientes/todos";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/nuevoPaciente";
    }


    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente) {
        pacienteRepository.save(paciente);
        return "redirect:/pacientes";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return "redirect:/pacientes";
    }
}

