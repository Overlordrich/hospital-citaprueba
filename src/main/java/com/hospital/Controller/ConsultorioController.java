package com.hospital.Controller;

import com.hospital.entity.Consultorio;
import com.hospital.repository.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consultorios")
@RequiredArgsConstructor
public class ConsultorioController {

    private final ConsultorioRepository consultorioRepository;


    @GetMapping
    public String listarConsultorios(Model model) {
        model.addAttribute("consultorios", consultorioRepository.findAll());
        return "Consultorios/todos";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("consultorio", new Consultorio());
        return "Consultorios/nuevoConsultorio";
    }


    @PostMapping("/guardar")
    public String guardarConsultorio(@ModelAttribute Consultorio consultorio) {
        consultorioRepository.save(consultorio);
        return "redirect:/Consultorios/todos";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarConsultorio(@PathVariable Long id) {
        consultorioRepository.deleteById(id);
        return "redirect:/Consultorios/todos";
    }
}
