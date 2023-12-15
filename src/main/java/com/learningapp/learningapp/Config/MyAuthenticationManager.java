package com.learningapp.learningapp.Config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MyAuthenticationManager implements AuthenticationManager {


    private List<GrantedAuthority> authorities;

    @Override
    public Authentication authenticate(Authentication authentication) {

        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Frontend"));
        User usr = new User(authentication.getPrincipal().toString(), authentication.getCredentials().toString(), authorities);
        Authentication auth1 = new UsernamePasswordAuthenticationToken("hola", authentication.getCredentials(), authorities);
        return auth1;

    }


}

