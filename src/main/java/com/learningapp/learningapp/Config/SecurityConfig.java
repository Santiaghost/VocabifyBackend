package com.learningapp.learningapp.Config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    public MyAuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authenticationManager(authenticationManager)
                .httpBasic(Customizer.withDefaults())
                .csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                        httpSecurityCsrfConfigurer.disable();
                    }
                })
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/usuario/images","/usuario/imagesUsername", "/apartado/images", "/api/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
