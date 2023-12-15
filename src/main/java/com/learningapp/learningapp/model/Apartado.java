package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "Apartados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apartado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "enunciado")
    private String enunciado;

    @JsonBackReference
    @ManyToOne
    private Ejercicio ejercicio;

    @ElementCollection
    @CollectionTable(name = "opciones_apartado", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "opciones")
    private List<String> opciones;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "imagenAdicional")
    private String imagenAdicional;


    public Apartado( String enunciado, List<String> opciones, Ejercicio ejercicio, String respuesta, String imagenAdicional){
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.ejercicio = ejercicio;
        this.respuesta = respuesta;
        this.imagenAdicional = imagenAdicional;
    }
}
