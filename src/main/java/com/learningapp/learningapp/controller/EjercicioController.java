package com.learningapp.learningapp.controller;

import com.learningapp.learningapp.model.*;
import com.learningapp.learningapp.service.EjercicioService;
import com.learningapp.learningapp.service.EjerciciosPublicadosService;
import com.learningapp.learningapp.service.ResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private EjerciciosPublicadosService ejerciciosPublicadosService;



    @PostMapping("/nuevo")
    public ResponseEntity<Ejercicio> nuevoEjercicio(@RequestBody Ejercicio ejercicio){
        Ejercicio devolver = ejercicioService.nuevoEjercicio(ejercicio, ejercicio.getApartados());
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Ejercicio> actualizarEjercicio(@RequestBody Ejercicio ejercicio){
        Ejercicio devolver = ejercicioService.actualizarEjercicio(ejercicio);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarEjercicio(@RequestParam("idEjercicio") long id){
        boolean devolver = resultadoService.borrarEjercicio(id);
        if(devolver != false){
            return new ResponseEntity<>("Borrado correctamente", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/corregir")
    public ResponseEntity<List<RespuestasApartado>> corregirEjericio(@RequestParam("idEjercicio") long idEjercicio, @RequestParam("userName") String username, @RequestBody List<RespuestasApartado> respuestasApartado){
        return new ResponseEntity<>(resultadoService.corregirEjercicio(respuestasApartado,username,idEjercicio), HttpStatus.OK);
    }

    @PostMapping("/publicarEjercicio")
    public ResponseEntity<EjerciciosPublicados> publicarEjercicio(@RequestParam("idEjercicio") long ejercicio, @RequestParam("userName") String username){
        EjerciciosPublicados devolver = ejerciciosPublicadosService.publicarEjercicio(ejercicio,username);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ejerciciosPublicados")
    public ResponseEntity<List<EjerciciosPublicados>> ejerciciosPublicados(){
        var result = ejerciciosPublicadosService.devolverTodosEjerciciosPublicados();
        return new ResponseEntity<>(ejerciciosPublicadosService.devolverTodosEjerciciosPublicados(), HttpStatus.OK);
    }

    @GetMapping("/ejerciciosPublicadosUsuario")
    public ResponseEntity<List<EjerciciosPublicados>> ejerciciosPublicados(@RequestParam("userName") String username){
        return new ResponseEntity<>(ejerciciosPublicadosService.devolverTodosEjerciciosPublicadosUsuario(username), HttpStatus.OK);
    }

    @GetMapping("/ejerciciosLikeadosUsuario")
    public ResponseEntity<List<EjerciciosPublicados>> ejerciciosLiked(@RequestParam("userName") String username){
        return new ResponseEntity<>(ejerciciosPublicadosService.devolverTodosEjerciciosUsuarioLiked(username), HttpStatus.OK);
    }
    @PostMapping("/likeEjercicio")
    public ResponseEntity<String> likeEjercicio(@RequestParam("idEjercicio") long idEjercicio , @RequestParam("userName") String username){
        boolean resultado = ejerciciosPublicadosService.DarLikeEjercicioPublicado(idEjercicio,username);
        if(resultado == true){
            return new ResponseEntity<>("Correcto", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Incorrecto", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/dislikeEjercicio")
    public ResponseEntity<String> dislikeEjercicio(@RequestParam("idEjercicio") long idEjercicio , @RequestParam("userName") String username){
        boolean resultado = ejerciciosPublicadosService.QuitarLikeEjercicioPublicado(idEjercicio,username);
        if(resultado == true){
            return new ResponseEntity<>("Correcto", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Incorrecto", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ejerciciosRecomendados")
    public ResponseEntity<Set<EjerciciosPublicados>> devolverEjerciciosRecomendados(@RequestParam("userName") String username){
        Set<EjerciciosPublicados> devolver = resultadoService.devolverEjerciciosRecomendados(username);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/estadisticasUsuario")
    public ResponseEntity<EstadisticasUsuario> devolverEstadisticasUsuario(@RequestParam("userName") String username){
        EstadisticasUsuario devolver = resultadoService.recuperarEstadisticasUsuario(username);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/resultadosUsuario")
    public ResponseEntity<List<Resultado>> devolverResultadosUsuario(@RequestParam("userName") String username){
        List<Resultado> devolver = resultadoService.recuperarResultadosUsuario(username);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}
