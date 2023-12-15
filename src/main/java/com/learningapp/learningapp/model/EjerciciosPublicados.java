package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;;

@Entity
@Table(name = "EjerciciosPublicados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjerciciosPublicados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "ejercicioId", unique = true)
    private Ejercicio ejercicioId;

    @Column(name = "likes")
    private int likes;

    @Column(name = "popularidad")
    private int popularidad;

    private String userName;

    public EjerciciosPublicados(Usuario usuario, Ejercicio ejercicioId, int likes, int popularidad) {
        this.usuario = usuario;
        this.ejercicioId = ejercicioId;
        this.likes = likes;
        this.popularidad = popularidad;
    }
}
