package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.EstadisticasUsuario;
import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadisticasUsuarioRepository extends JpaRepository<EstadisticasUsuario,Long> {
    Optional<EstadisticasUsuario> findByUsuario(Usuario usuario);
}
