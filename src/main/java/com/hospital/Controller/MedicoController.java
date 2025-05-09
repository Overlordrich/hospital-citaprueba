package com.hospital.Controller;

import com.hospital.entity.Medico;
import com.hospital.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoRepository medicoRepository;


    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());
        return "Medicos/todos";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("medico", new Medico());
        return "Medicos/nuevoMedico";
    }


    @PostMapping("/guardar")
    public String guardarMedico(@ModelAttribute Medico medico) {
        medicoRepository.save(medico);
        return "redirect:/medicos";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable Long id) {
        medicoRepository.deleteById(id);
        return "redirect:/medicos";
    }
}

