package com.learningapp.learningapp.controller;

import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.Usuario;
import com.learningapp.learningapp.service.UsuarioService;
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
import java.util.List;

@RestController
@RequestMapping("/usuario")
@PropertySource("classpath:application.properties")
public class UsuarioController {


    @Autowired
    public UsuarioService usuarioService;

    @Value("${paths.pathUserImage}")
    private String userImagePath;



    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody Usuario usuario){
        String respuesta = usuarioService.Login(usuario.getUsername(), usuario.getPassword());
        if(respuesta.equals("Sin permisos")){
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody Usuario usuario){
        String respuesta = usuarioService.Register(usuario);
        if(respuesta.equals("Usuario o email ya registrados")){
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
    }

    @GetMapping("/images")
    public ResponseEntity<Resource> getUserImage(@RequestParam String imageName) {
        Path filePath = Paths.get(userImagePath, imageName);
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/imagesUsername")
    public ResponseEntity<Resource> getUserImageByUserName(@RequestParam("userName") String userName) {
        Usuario u = usuarioService.findUserByUserName(userName);
        Path filePath = Paths.get("C:\\Users\\santi\\TFG\\frontend\\frontend\\assets\\images\\usuarios");
        if(u != null) {
            if (u.getNombreImagen() != null) {
                filePath = Paths.get(userImagePath, u.getNombreImagen());
            } else {
                filePath = Paths.get(userImagePath, "profile.png");
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> subirImagen(@RequestParam("imagen") MultipartFile file, @RequestParam("username") String userName) {
        if (!file.isEmpty()) {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String rutaDirectorio = userImagePath;
            String nombreArchivo = userName.replace(".","") + extension;
            System.out.println(userImagePath);
            System.out.println(nombreArchivo);
            try {
                File archivo = new File(rutaDirectorio, nombreArchivo);
                file.transferTo(archivo);
                usuarioService.subirImagen(userName,nombreArchivo);
                return new ResponseEntity<>("Subida", HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Archivo no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Usuario usuario){
        if(usuarioService.updateUser(usuario)){
            return new ResponseEntity<>("Actualizado correctamente", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Error al actualizar usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/addExercise")
    public ResponseEntity<Ejercicio> addExercise(@RequestParam("username") String username, @RequestBody Ejercicio ejercicio){
        Ejercicio resultado = usuarioService.addExercise(username,ejercicio);
        if(resultado != null){
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getExercises")
    public ResponseEntity<List<Ejercicio>> getExercises(@RequestParam("username") String username){
        List<Ejercicio> resultado = usuarioService.obtenerEjerciciosUsuario(username);
        if(resultado != null){
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
