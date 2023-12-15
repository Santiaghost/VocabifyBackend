package com.learningapp.learningapp.service;

import com.learningapp.learningapp.model.*;
import com.learningapp.learningapp.repository.EstadisticasUsuarioRepository;
import com.learningapp.learningapp.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApartadoService apartadoService;

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private EstadisticasUsuarioRepository estadisticasUsuarioRepository;

    @Autowired
    private EjerciciosPublicadosService ejerciciosPublicadosService;


    public List<RespuestasApartado> corregirEjercicio(List<RespuestasApartado> respuestasApartado, String userName, long idEjercicio){
        List<RespuestasApartado> resultado = new ArrayList<RespuestasApartado>();
        Usuario usuario = usuarioService.findUserByUserName(userName);
        Ejercicio ejercicio = ejercicioService.findById(idEjercicio);
        double nota = 0;
        for (RespuestasApartado respuestaApartado: respuestasApartado) {
            Apartado correcto = apartadoService.devolverApartado(respuestaApartado.idApartado);
            String respuestaCorrecta = correcto.getRespuesta().toUpperCase();
            if(respuestaCorrecta.contains(respuestaApartado.respuesta.toUpperCase())){
                RespuestasApartado completar = new RespuestasApartado(correcto.getId(),respuestaApartado.getRespuesta(), correcto.getRespuesta(),"OK");
                resultado.add(completar);
                nota += 1;
            }
            else if(respuestaApartado.respuesta.equals("")){
                RespuestasApartado completar = new RespuestasApartado(correcto.getId(),respuestaApartado.getRespuesta(), correcto.getRespuesta(),"NC");
                resultado.add(completar);
            }
            else{
                RespuestasApartado completar = new RespuestasApartado(correcto.getId(),respuestaApartado.getRespuesta(), correcto.getRespuesta(),"MAL");
                resultado.add(completar);
            }
        }
        nota = nota/respuestasApartado.size() * 10;

        Resultado res = new Resultado(usuario,ejercicio,nota);

        Resultado buscar = findByUserNameExercise(usuario,ejercicio);

        if(buscar != null){
            res.setId(buscar.getId());
        }
        actualizarEstadisticasUsuario(usuario);
        resultadoRepository.save(res);

        return resultado;
    }

    public Set<EjerciciosPublicados> devolverEjerciciosRecomendados(String userName){
        Usuario u = usuarioService.findUserByUserName(userName);
        Set<EjerciciosPublicados> ejerciciosPublicadosRecomendar = new HashSet<>();
        List<EjerciciosPublicados> todosEjerciciosPublicados = ejerciciosPublicadosService.devolverTodosEjerciciosPublicados();
        if(u != null){
            List<Resultado> todosResultadosSuspensos = recuperarResultadosUsuario(u.getUsername()).stream().filter(obj -> obj.getNota() < 5).collect(Collectors.toList());
            for(Resultado r : todosResultadosSuspensos){
                switch (r.getEjercicioId().getNivel()){
                    case "Básico":
                        ejerciciosPublicadosRecomendar.addAll(todosEjerciciosPublicados.stream().filter(obj -> (obj.getEjercicioId().getNivel().equals("Básico") || obj.getEjercicioId().getNivel().equals("Fácil")) && obj.getEjercicioId().getIdioma().equals(r.getEjercicioId().getIdioma())).collect(Collectors.toList()));
                    case "Fácil":
                        ejerciciosPublicadosRecomendar.addAll(todosEjerciciosPublicados.stream().filter(obj -> (obj.getEjercicioId().getNivel().equals("Básico") || obj.getEjercicioId().getNivel().equals("Fácil") || obj.getEjercicioId().getNivel().equals("Intermedio")) && obj.getEjercicioId().getIdioma().equals(r.getEjercicioId().getIdioma())).collect(Collectors.toList()));
                    case "Intermedio":
                        ejerciciosPublicadosRecomendar.addAll(todosEjerciciosPublicados.stream().filter(obj -> (obj.getEjercicioId().getNivel().equals("Difícil") || obj.getEjercicioId().getNivel().equals("Fácil") || obj.getEjercicioId().getNivel().equals("Intermedio")) && obj.getEjercicioId().getIdioma().equals(r.getEjercicioId().getIdioma())).collect(Collectors.toList()));
                    case "Difícil":
                        ejerciciosPublicadosRecomendar.addAll(todosEjerciciosPublicados.stream().filter(obj -> (obj.getEjercicioId().getNivel().equals("Difícil") || obj.getEjercicioId().getNivel().equals("Avanzado") || obj.getEjercicioId().getNivel().equals("Intermedio")) && obj.getEjercicioId().getIdioma().equals(r.getEjercicioId().getIdioma())).collect(Collectors.toList()));
                    case "Avanzado":
                        ejerciciosPublicadosRecomendar.addAll(todosEjerciciosPublicados.stream().filter(obj -> (obj.getEjercicioId().getNivel().equals("Difícil") || obj.getEjercicioId().getNivel().equals("Avanzado")) && obj.getEjercicioId().getIdioma().equals(r.getEjercicioId().getIdioma())).collect(Collectors.toList()));
                }
            }
            return ejerciciosPublicadosRecomendar;
        }
        else{
            return null;
        }
    }

    public static String devolverRepeticionesResultados(List<Resultado> list, String valor) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (Resultado obj : list) {
            if(valor.equals("Nivel")){
                String valueB = obj.getEjercicioId().getNivel();
                frequencyMap.put(valueB, frequencyMap.getOrDefault(valueB, 0) + 1);
            }
            if(valor.equals("Idioma")){
                String valueB = obj.getEjercicioId().getIdioma();
                frequencyMap.put(valueB, frequencyMap.getOrDefault(valueB, 0) + 1);
            }
            if(valor.equals("TipoEjercicio")){
                String valueB = obj.getEjercicioId().getTipoejercicio();
                frequencyMap.put(valueB, frequencyMap.getOrDefault(valueB, 0) + 1);
            }
        }

        int frecuencia = 0;
        String masRepetido = null;

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > frecuencia) {
                frecuencia = entry.getValue();
                masRepetido = entry.getKey();
            }
        }

        return masRepetido;
    }

    public List<Resultado> recuperarResultadosUsuario(String username){
        Usuario u = usuarioService.findUserByUserName(username);
        if(u != null){
            return resultadoRepository.findResultadoByUser(u);
        }
        else{
            return null;
        }
    }

    public void borrarResultadosEjercicio(Ejercicio ejercicioId){
        resultadoRepository.deleteByEjercicioId(ejercicioId);
    }



    public void actualizarEstadisticasUsuario(Usuario u){
        EstadisticasUsuario e = recuperarEstadisticasUsuario(u.getUsername());
        List<Resultado> resultadoList = recuperarResultadosUsuario(u.getUsername());
        String tipoEjercicioMasHabituado = devolverRepeticionesResultados(resultadoList,"TipoEjercicio");
        String nivelMasHabituado = devolverRepeticionesResultados(resultadoList,"Nivel");
        String idiomaMasHabituado = devolverRepeticionesResultados(resultadoList,"Idioma");
        if(e != null){
            e.setIdiomaPreferido(idiomaMasHabituado);
            e.setNivelPreferido(nivelMasHabituado);
            e.setTipoPreferido(tipoEjercicioMasHabituado);
            estadisticasUsuarioRepository.save(e);
        }
        else{
            EstadisticasUsuario nueva = new EstadisticasUsuario(u,idiomaMasHabituado,tipoEjercicioMasHabituado,nivelMasHabituado);
            estadisticasUsuarioRepository.save(nueva);
        }


    }

    public EstadisticasUsuario recuperarEstadisticasUsuario(String username){
        Usuario u = usuarioService.findUserByUserName(username);
        if(u != null){
            return estadisticasUsuarioRepository.findByUsuario(u).orElse(new EstadisticasUsuario(null,"","",""));
        }
        {
            return new EstadisticasUsuario(null,"","","");
        }
    }

    public Resultado findByUserNameExercise(Usuario u, Ejercicio e){
        List<Resultado> resultados = resultadoRepository.findResultadoByUserAndEjercicioId(u,e);
        if(resultados.size() > 0){
            return resultados.get(0);
        }
        return null;
    }

    public boolean borrarEjercicio(long id){
        Ejercicio e = ejercicioService.findById(id);
        if(e != null){
            borrarResultadosEjercicio(e);
            ejerciciosPublicadosService.QuitarTodosLikesEjercicio(e);
            ejerciciosPublicadosService.eliminarEjercicioPublicado(id);
            ejercicioService.borrarEjercicio(id);
            return true;
        }
        return false;
    }


}
