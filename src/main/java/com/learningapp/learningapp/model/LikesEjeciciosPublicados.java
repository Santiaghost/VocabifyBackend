package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "LikesEjerciciosPublicados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikesEjeciciosPublicados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ejercicioId")
    private Ejercicio ejercicioId;

    public LikesEjeciciosPublicados(Usuario usuario, Ejercicio ejercicioId) {
        this.usuario = usuario;
        this.ejercicioId = ejercicioId;
    }
}
