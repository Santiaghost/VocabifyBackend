package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EjercicioRepository extends JpaRepository<Ejercicio,Long> {
    @Transactional
    Optional<Ejercicio> findById(long id);
}
