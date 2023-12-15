package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.EjerciciosPublicados;
import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EjerciciosPublicadosRepository extends JpaRepository<EjerciciosPublicados,Long> {

    @Transactional
    Optional<EjerciciosPublicados> findByEjercicioId(Ejercicio ejercicioId);

    @Transactional
    List<EjerciciosPublicados> findByUsuario(Usuario usuario);

}
