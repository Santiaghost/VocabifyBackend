package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.Apartado;
import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ApartadoRepository extends JpaRepository<Apartado, Long> {
    @Transactional
    Optional<Apartado> findById(long id);
}
