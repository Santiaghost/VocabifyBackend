package com.learningapp.learningapp.repository;


import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.LikesEjeciciosPublicados;
import com.learningapp.learningapp.model.Resultado;
import com.learningapp.learningapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikesEjerciciosPublicadosRepository extends JpaRepository<LikesEjeciciosPublicados,Long> {
    @Transactional
    List<LikesEjeciciosPublicados> findResultadoByUsuarioAndEjercicioId(Usuario usuario, Ejercicio ejercicioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikesEjeciciosPublicados t WHERE t.ejercicioId = :ejercicioId")
    void deleteByEjercicioId(Ejercicio ejercicioId);
}
