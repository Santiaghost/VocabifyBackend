package com.learningapp.learningapp.controller;

import com.learningapp.learningapp.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:application.properties")
public class ApiController {

    private static final String SECRET_KEY = "ClaveSecretaSuperSeguraMegaSeguridad";

    @Value("${apiCredentials.username}")
    private String apiUsername;

    @Value("${apiCredentials.password}")
    private String apiPasword;

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody Usuario usuario){
        String respuesta = "";
        if(usuario.getUsername().equals(apiUsername) && usuario.getPassword().equals(apiPasword)){
            Date issuedDate = new Date();
            Date expirationDate = new Date(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000000000));

            Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

            Map<String, Object> claims = new HashMap<>();


            String jwtToken = Jwts.builder()
                    .setSubject(usuario.getUsername())
                    .setIssuedAt(issuedDate)
                    .setExpiration(expirationDate)
                    .addClaims(claims)
                    .signWith(key,SignatureAlgorithm.HS256)
                    .compact();

            respuesta = jwtToken;
        }
        if(respuesta.equals("")){
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
    }

}
