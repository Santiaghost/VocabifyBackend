package com.learningapp.learningapp.controller;

import com.learningapp.learningapp.model.Apartado;
import com.learningapp.learningapp.service.ApartadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/apartado")
@PropertySource("classpath:application.properties")
public class ApartadoController {

    @Autowired
    private ApartadoService apartadoService;


    @Value("${paths.pathApartadoImage}")
    private String apartadoImagePath;

    @PostMapping("/nuevo")
    public ResponseEntity<Apartado> nuevoApartado(@RequestBody Apartado apartado){
        Apartado devolver = apartadoService.nuevoApartado(apartado);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Apartado> actualizarApartado(@RequestBody Apartado apartado){
        Apartado devolver = apartadoService.actualizarApartado(apartado);
        if(devolver != null){
            return new ResponseEntity<>(devolver, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(devolver, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/borrar")
    public ResponseEntity<String > borrarApartado(@RequestBody Apartado apartado){
        Apartado devolver = apartadoService.actualizarApartado(apartado);
        if(devolver != null){
            return new ResponseEntity<>("El apartado se borró correctamente", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Error al borrar apartado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/images")
    public ResponseEntity<Resource> getApartadoImage(@RequestParam String imageName) {
        Path filePath = Paths.get(apartadoImagePath, imageName);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subirImagen")
    public ResponseEntity<String > subirImagen(@RequestParam("imagen") MultipartFile file, @RequestParam("idApartado") long idApartado){
        if (!file.isEmpty()) {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String rutaDirectorio = apartadoImagePath;
            String nombreArchivo = "Apartado-" + idApartado + extension;

            try {
                File archivo = new File(rutaDirectorio, nombreArchivo);
                file.transferTo(archivo);
                apartadoService.subirImagen(nombreArchivo,idApartado);
                return new ResponseEntity<>("Subida", HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Archivo no encontrado", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/leer")
    public ResponseEntity<Apartado> leerApartado(@RequestParam int id){
        return new ResponseEntity<>(apartadoService.devolverApartado(id), HttpStatus.OK);
    }
}
