package com.learningapp.learningapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "fotoPerfil")
    private String nombreImagen;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Ejercicio> ejercicios;

    public Usuario(String username, String email, String password, String phone, String nombreImagen, List<Ejercicio> ejercicios){
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nombreImagen = nombreImagen;
        this.ejercicios = ejercicios;
    }



}
