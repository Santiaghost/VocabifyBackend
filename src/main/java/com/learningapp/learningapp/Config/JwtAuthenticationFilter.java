package com.learningapp.learningapp.Config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class JwtAuthenticationFilter implements Filter {

    private static final String SECRET_KEY = "ClaveSecretaSuperSeguraMegaSeguridad";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = extractToken((HttpServletRequest) servletRequest);
        boolean valid = isTokenValid(token);
        if(valid || isAllowedRequest(((HttpServletRequest) servletRequest).getRequestURI())){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no válido");
        }
    }

    public static boolean isTokenValid(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractToken(HttpServletRequest servletRequest){
        String heather = servletRequest.getHeader("Authorization");
        if(heather != null){
            heather = decodeBasicAuth(heather);
            return heather;
        }
        return "";
    }

    public boolean isAllowedRequest(String uri){
        return uri.contains("/usuario/imagesUsername") || uri.contains("/api/login") || uri.contains("/apartado/images") || uri.contains("/usuario/images");
    }


    private static String decodeBasicAuth(String encodedCredentials) {
        String base64Credentials = encodedCredentials.replace("Basic ", "");

        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);

        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
