package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ApellidoP;
    private String ApellidoM;
    private String especialidad;
}

