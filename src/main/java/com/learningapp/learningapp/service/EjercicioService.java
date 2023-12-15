package com.learningapp.learningapp.service;

import com.learningapp.learningapp.model.Apartado;
import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private ApartadoService apartadoService;




    public Ejercicio nuevoEjercicio(Ejercicio ejercicio, List<Apartado> apartadoList){
        for (Apartado a : apartadoList) {
            Apartado aux = a;
            aux.setEjercicio(ejercicio);
            apartadoService.nuevoApartado(aux);
        }
        Ejercicio devolver = ejercicioRepository.save(ejercicio);
        return devolver;
    }

    public Ejercicio actualizarEjercicio(Ejercicio ejercicio){
        Ejercicio e = findById(ejercicio.getId());
        if(e != null){
            for (Apartado a : ejercicio.getApartados()) {
                apartadoService.actualizarApartado(a);
            }
            ejercicio.setUsuario(e.getUsuario());
            ejercicioRepository.save(ejercicio);
            return ejercicio;
        }
        return null;
    }

    public boolean borrarEjercicio(long id){
        Ejercicio e = findById(id);
        if(e != null){
            for (Apartado a : e.getApartados()) {
                apartadoService.borrarApartado(a.getId());
            }
            ejercicioRepository.delete(e);
            return true;
        }
        return false;
    }

    public Ejercicio findById(long id){
        return ejercicioRepository.findById(id).orElse(null);
    }


}
