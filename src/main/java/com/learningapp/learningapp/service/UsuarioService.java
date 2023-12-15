package com.learningapp.learningapp.service;

import com.learningapp.learningapp.model.Apartado;
import com.learningapp.learningapp.model.Ejercicio;
import com.learningapp.learningapp.model.Usuario;
import com.learningapp.learningapp.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioService {

    private static final String SECRET_KEY = "ClaveSecretaSuperSeguraMegaSeguridad";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EjercicioService ejercicioService;



    public boolean verificarPassword(String password1, String password2, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password1,password2);
    }

    public String Login(String username, String password){
        Usuario u = findUserByUserName(username);
        PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
        if(u != null){
            boolean verificar = verificarPassword(password,u.getPassword(), pwdEncoder);
            if(verificar){
                Date issuedDate = new Date();
                Date expirationDate = new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000000000));

                Key key = new SecretKeySpec(SECRET_KEY.getBytes(),SignatureAlgorithm.HS256.getJcaName());

                Map<String, Object> claims = new HashMap<>();
                claims.put("nombreUsuario", u.getUsername());
                claims.put("correo", u.getEmail());
                claims.put("telefono", u.getPhone() != null ? u.getPhone() : "");
                claims.put("imagenPerfil", u.getNombreImagen() != null ? u.getNombreImagen() : "");


                String jwtToken = Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(issuedDate)
                        .setExpiration(expirationDate)
                        .addClaims(claims)
                        .signWith(key,SignatureAlgorithm.HS256)
                        .compact();

                return jwtToken;
            }
        }
        return "Sin permisos";
    }


    public String Register(Usuario usuario){
        Usuario u = findUserByUserName(usuario.getUsername());
        Usuario u2 = findUserByEmail(usuario.getEmail());
        if(u == null && u2 == null){
            usuarioRepository.save(usuario);
            Date issuedDate = new Date();
            Date expirationDate = new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000000000));

            Key key = new SecretKeySpec(SECRET_KEY.getBytes(),SignatureAlgorithm.HS256.getJcaName());

            Map<String, Object> claims = new HashMap<>();
            claims.put("nombreUsuario", usuario.getUsername());
            claims.put("correo", usuario.getEmail());
            claims.put("telefono", usuario.getPhone() != null ? usuario.getPhone() : "");
            claims.put("imagenPerfil",  usuario.getNombreImagen() != null ? usuario.getNombreImagen() : "");

            String jwtToken = Jwts.builder()
                    .setSubject(usuario.getUsername())
                    .setIssuedAt(issuedDate)
                    .setExpiration(expirationDate)
                    .addClaims(claims)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return jwtToken;
        }
        else{
            return "Usuario o email ya registrados";
        }
    }

    @Transactional
    public Ejercicio addExercise (String username, Ejercicio ejercicio){
        Usuario u = findUserByUserName(username);
        if(u != null){
            List<Ejercicio> ejerciciosUsuario = u.getEjercicios();
            List<Apartado> apartadosEjercicio = ejercicio.getApartados();
            ejercicio.setUsuario(u);
            ejercicioService.nuevoEjercicio(ejercicio, apartadosEjercicio);
            ejercicio.setApartados(apartadosEjercicio);
            ejerciciosUsuario.add(ejercicio);
            u.setEjercicios(ejerciciosUsuario);
            boolean resultado = updateUser(u);
            if(resultado){
                return ejercicio;
            }
            else{
                return null;
            }

        }
        else{
            return null;
        }

    }

    @Transactional
    public List<Ejercicio> obtenerEjerciciosUsuario(String username){
        Usuario u = findUserByUserName(username);
        if(u != null ){
            return u.getEjercicios();
        }
        else{
            return null;
        }
    }


    @Transactional
    public Usuario findUserByUserName(String username){
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    public Usuario findUserByEmail(String email){
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public boolean updateUser(Usuario usuario){
        Usuario u = findUserByUserName(usuario.getUsername());
        if (u != null){
            Usuario cambiar = u;
            if(usuario.getEmail() != ""){
                cambiar.setEmail(usuario.getEmail());
            }
            if(usuario.getPassword() != ""){
                cambiar.setPassword(usuario.getPassword());
            }
            if(usuario.getNombreImagen() != "" && usuario.getNombreImagen() != null){
                cambiar.setNombreImagen(usuario.getNombreImagen());
            }
            if(usuario.getPhone() != ""){
                cambiar.setPhone(usuario.getPhone());
            }
            usuarioRepository.save(cambiar);
            return true;
        }
        return false;
    }

    public void subirImagen(String username, String nombreImagen){
        Usuario u = findUserByUserName(username);
        if(u != null){
            Usuario cambiar = u;
            cambiar.setNombreImagen(nombreImagen);
            updateUser(cambiar);
        }
    }


}
