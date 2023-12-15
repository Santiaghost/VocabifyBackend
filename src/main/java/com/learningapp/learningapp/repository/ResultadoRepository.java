package com.learningapp.learningapp.repository;

import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.Resultado;
import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    @Transactional
    List<Resultado> findResultadoByUserAndEjercicioId(Usuario user, Ejercicio ejercicioId);

    @Transactional
    List<Resultado> findResultadoByUser(Usuario user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Resultado t WHERE t.ejercicioId = :ejercicio")
    void deleteByEjercicioId(Ejercicio ejercicio);
}
