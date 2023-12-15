package com.learningapp.learningapp.service;
import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.EjerciciosPublicados;
import com.learningapp.learningapp.model.LikesEjeciciosPublicados;
import com.learningapp.learningapp.model.Usuario;
import com.learningapp.learningapp.repository.EjerciciosPublicadosRepository;
import com.learningapp.learningapp.repository.LikesEjerciciosPublicadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EjerciciosPublicadosService {

    @Autowired
    private EjerciciosPublicadosRepository ejerciciosPublicadosRepository;

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LikesEjerciciosPublicadosRepository likesEjerciciosPublicadosRepository;


    public EjerciciosPublicados publicarEjercicio(long ejercicioId, String username){
        Usuario u = usuarioService.findUserByUserName(username);
        Ejercicio e = ejercicioService.findById(ejercicioId);

        if(u != null && e != null){
            EjerciciosPublicados nuevo = new EjerciciosPublicados(u,e,0,0);
            ejerciciosPublicadosRepository.save(nuevo);
            return nuevo;
        }
        return null;
    }

    public EjerciciosPublicados findById(long id){
        Ejercicio e = ejercicioService.findById(id);
        return ejerciciosPublicadosRepository.findByEjercicioId(e).orElse(null);
    }

    public boolean DarLikeEjercicioPublicado (long ejercicioId, String username){
        EjerciciosPublicados e = findById(ejercicioId);
        if(e != null){
            Ejercicio ej = ejercicioService.findById(ejercicioId);
            Usuario u = usuarioService.findUserByUserName(username);
            if(likesEjerciciosPublicadosRepository.findResultadoByUsuarioAndEjercicioId(u,ej).isEmpty()){
                e.setLikes(e.getLikes() + 1);
                LikesEjeciciosPublicados nuevo = new LikesEjeciciosPublicados(u,ej);
                likesEjerciciosPublicadosRepository.save(nuevo);
                ejerciciosPublicadosRepository.save(e);
                return true;
            }
        }
        return false;
    }

    public boolean QuitarLikeEjercicioPublicado (long ejercicioId, String username){
        EjerciciosPublicados e = findById(ejercicioId);
        if(e != null){
            Ejercicio ej = ejercicioService.findById(ejercicioId);
            Usuario u = usuarioService.findUserByUserName(username);
            if(!likesEjerciciosPublicadosRepository.findResultadoByUsuarioAndEjercicioId(u,ej).isEmpty()){
                e.setLikes(e.getLikes() - 1);
                LikesEjeciciosPublicados eliminar = likesEjerciciosPublicadosRepository.findResultadoByUsuarioAndEjercicioId(u,ej).get(0);
                likesEjerciciosPublicadosRepository.delete(eliminar);
                ejerciciosPublicadosRepository.save(e);
                return true;
            }
        }
        return false;
    }

    public void QuitarTodosLikesEjercicio(Ejercicio ejercicioId){
        likesEjerciciosPublicadosRepository.deleteByEjercicioId(ejercicioId);
    }


    public List<EjerciciosPublicados> devolverTodosEjerciciosPublicadosUsuario(String username){
        return devolverTodosEjerciciosPublicados().stream().filter(c -> c.getUsuario().getUsername().equals(username)).collect(Collectors.toList());
    }

    public List<EjerciciosPublicados> devolverTodosEjerciciosUsuarioLiked(String username){
        List<LikesEjeciciosPublicados> todosEjerciciosLike = likesEjerciciosPublicadosRepository.findAll();
        List<EjerciciosPublicados> result = new ArrayList<>();
        List<EjerciciosPublicados> todosEjerciciosPublicados = devolverTodosEjerciciosPublicados();
        for (LikesEjeciciosPublicados l: todosEjerciciosLike) {
            EjerciciosPublicados ej = findById(l.getEjercicioId().getId());
            result.add(ej);
        }
        return result;
    }

    public void eliminarEjercicioPublicado(long ejercicioId){
        EjerciciosPublicados e = findById(ejercicioId);
        if(e != null){
            ejerciciosPublicadosRepository.delete(e);
        }
    }

    public List<EjerciciosPublicados> devolverTodosEjerciciosPublicados(){
        List<EjerciciosPublicados> result = ejerciciosPublicadosRepository.findAll();
        List<EjerciciosPublicados> aux = new ArrayList<>();
        for(EjerciciosPublicados ej : result){
            EjerciciosPublicados auxEntrar = ej;
            auxEntrar.setUserName(ej.getUsuario().getUsername());
            aux.add(auxEntrar);
        }
        return aux;
    }



}
