package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EstadisticasUsuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", unique = true)
    private Usuario usuario;

    @Column(name = "IdiomaPreferido")
    public String idiomaPreferido;

    @Column(name = "TipoPreferido")
    public String tipoPreferido;

    @Column(name = "NivelPreferido")
    public String nivelPreferido;


    public EstadisticasUsuario(Usuario usuario, String idiomaPreferido, String tipoPreferido, String nivelPreferido) {
        this.usuario = usuario;
        this.idiomaPreferido = idiomaPreferido;
        this.tipoPreferido = tipoPreferido;
        this.nivelPreferido = nivelPreferido;

    }
}
