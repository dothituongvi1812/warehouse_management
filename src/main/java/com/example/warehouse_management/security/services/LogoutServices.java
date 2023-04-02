package com.example.warehouse_management.security.services;

import com.example.warehouse_management.security.jwt.AuthTokenFilter;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutServices implements LogoutHandler {
    @Value("${warehouse.app.jwtSecret}")
    private String jwtSecret;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt= AuthTokenFilter.parseJwt(request);
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
        Jwts.builder().setExpiration(new Date(new Date().getTime()-10000));

    }
}
