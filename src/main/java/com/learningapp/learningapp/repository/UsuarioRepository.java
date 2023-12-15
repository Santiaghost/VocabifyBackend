package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Transactional
    Optional<Usuario> findByUsername(String s);
    @Transactional
    Optional<Usuario> findByEmail(String s);
}
