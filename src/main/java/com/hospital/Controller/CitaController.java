package com.hospital.Controller;

import com.hospital.entity.Cita;
import com.hospital.entity.Consultorio;
import com.hospital.entity.Medico;
import com.hospital.entity.Paciente;
import com.hospital.repository.CitaRepository;
import com.hospital.repository.ConsultorioRepository;
import com.hospital.repository.MedicoRepository;
import com.hospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultorioRepository consultorioRepository;


    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("medicos", medicoRepository.findAll());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("consultorios", consultorioRepository.findAll());
        return "alta";
    }


    @PostMapping("/agendar")
    public String agendarCita(@ModelAttribute Cita cita, Model model) {

        Medico doctor = medicoRepository.findById(cita.getDoctor().getId()).orElseThrow();
        Paciente paciente = pacienteRepository.findById(cita.getPaciente().getId()).orElseThrow();
        Consultorio consultorio = consultorioRepository.findById(cita.getConsultorio().getId()).orElseThrow();

        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setConsultorio(consultorio);

        if (!validarCita(cita, paciente, doctor, consultorio)) {
            model.addAttribute("error", "Error en la validación de la cita. Revisa las reglas.");
            return "alta";
        }

        citaRepository.save(cita);
        return "redirect:/citas/consulta";
    }



    @GetMapping("/consulta")
    public String consultarCitas(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String doctorNombre,
            @RequestParam(required = false) String consultorioNumero,
            Model model) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusSeconds(1);

        if (fecha != null && !fecha.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(fecha, formatter);
            start = date.atStartOfDay();
            end = start.plusDays(1).minusSeconds(1);
        }

        List<Cita> citas = citaRepository.findByHorarioBetween(start, end);

        if (doctorNombre != null && !doctorNombre.isEmpty()) {
            Medico doctor = medicoRepository.findByNombre(doctorNombre);
            if (doctor != null) {
                citas = citas.stream()
                        .filter(c -> c.getDoctor().getId().equals(doctor.getId()))
                        .toList();
            }
        }

        if (consultorioNumero != null && !consultorioNumero.isEmpty()) {
            Consultorio consultorio = consultorioRepository.findByNumeroConsultorio(consultorioNumero);
            if (consultorio != null) {
                citas = citas.stream()
                        .filter(c -> c.getConsultorio().getId().equals(consultorio.getId()))
                        .toList();
            }
        }

        model.addAttribute("citas", citas);
        return "consulta";
    }




    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        model.addAttribute("cita", cita);
        model.addAttribute("medicos", medicoRepository.findAll());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("consultorios", consultorioRepository.findAll());
        return "editarCita";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, @ModelAttribute Cita citaEditada, Model model) {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        // Validar reglas: No duplicar horarios de doctor ni de consultorio
        boolean doctorOcupado = citaRepository.existsByDoctorAndHorario(citaEditada.getDoctor(), citaEditada.getHorario());
        boolean consultorioOcupado = citaRepository.existsByConsultorioAndHorario(citaEditada.getConsultorio(), citaEditada.getHorario());

        if (doctorOcupado || consultorioOcupado) {
            model.addAttribute("error", "Doctor o consultorio ya ocupado para ese horario.");
            return "editarCita";
        }

        citaExistente.setHorario(citaEditada.getHorario());
        citaExistente.setDoctor(citaEditada.getDoctor());
        citaExistente.setPaciente(citaEditada.getPaciente());
        citaExistente.setConsultorio(citaEditada.getConsultorio());

        citaRepository.save(citaExistente);
        return "redirect:/citas/consulta";
    }



    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, Model model) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (cita.getHorario().isAfter(LocalDateTime.now())) {
            citaRepository.deleteById(id);
        } else {
            model.addAttribute("error", "No se puede cancelar una cita ya realizada.");
        }

        return "redirect:/citas/consulta";
    }




    private boolean validarCita(Cita nuevaCita, Paciente paciente, Medico doctor, Consultorio consultorio) {
        LocalDateTime horario = nuevaCita.getHorario();
        LocalDateTime inicioDia = horario.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        // No se puede agendar cita en un mismo consultorio a la misma hora.
        boolean conflictoConsultorio = citaRepository.existsByConsultorioAndHorario(consultorio, horario);

        // No se puede agendar cita para un mismo Dr. a la misma hora
        boolean conflictoDoctor = citaRepository.existsByDoctorAndHorario(doctor, horario);

        // no se puede agendar cita para un paciente a la una misma hora ni con menos de 2 horas
        //de diferencia para el mismo día. Es decir, si un paciente tiene 1 cita de 2 a 3pm, solo
        //podría tener otra el mismo día a partir de las 5pm
        List<Cita> citasPaciente = citaRepository.findByPacienteAndHorarioBetween(paciente, inicioDia, finDia);
        boolean conflictoPaciente = citasPaciente.stream()
                .anyMatch(cita -> Math.abs(cita.getHorario().getHour() - horario.getHour()) < 2);

        // Un mismo doctor no puede tener más de 8 citas en el día.
        List<Cita> citasDoctor = citaRepository.findByDoctorAndHorarioBetween(doctor, inicioDia, finDia);
        boolean excesoDoctor = citasDoctor.size() >= 8;

        return !(conflictoConsultorio || conflictoDoctor || conflictoPaciente || excesoDoctor);
    }

}
