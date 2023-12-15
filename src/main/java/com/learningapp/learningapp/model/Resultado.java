package com.learningapp.learningapp.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Resultados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "userId")
    private Usuario user;

    @OneToOne
    @JoinColumn(name = "ejercicioId")
    private Ejercicio ejercicioId;

    @Column(name = "nota")
    private double nota;

    public Resultado(Usuario user, Ejercicio ejercicioId, double nota) {
        this.user = user;
        this.ejercicioId = ejercicioId;
        this.nota = nota;
    }
}
