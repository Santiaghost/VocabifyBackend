package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Ejercicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @JsonManagedReference
    @OneToMany(mappedBy = "ejercicio")
    private List<Apartado> apartados;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "nivel")
    private String nivel;

    @Column(name = "tipoejercicio")
    private String tipoejercicio;

    @JsonBackReference
    @ManyToOne
    private Usuario usuario;

    public Ejercicio(String titulo, List<Apartado> apartados, String idioma, String nivel, String tipoejercicio) {
        this.titulo = titulo;
        this.apartados = apartados;
        this.idioma = idioma;
        this.nivel = nivel;
        this.tipoejercicio = tipoejercicio;
    }
}
