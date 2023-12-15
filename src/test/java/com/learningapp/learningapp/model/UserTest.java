package com.learningapp.learningapp.model;

import com.learningapp.learningapp.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void addUser(){
        Usuario u = new Usuario("pedro.garcia", "pedro@email.com", "password", null, null,null);
        usuarioRepository.save(u);
    }
}
