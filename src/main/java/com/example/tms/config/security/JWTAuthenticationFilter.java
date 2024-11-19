package com.example.tms.config.security;

import com.example.tms.exceptions.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final ObjectMapper objectMapper;

    @Autowired
    public JWTAuthenticationFilter(JwtTokenUtils jwtTokenUtils, ObjectMapper objectMapper) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException e) {
                ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED,
                        "JWT token is expired", null);
                writeErrorToResponse(response, errorResponse);
                return;
            } catch (Exception e) {
                ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED,
                        "Wrong JWT token", null);
                writeErrorToResponse(response, errorResponse);
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private void writeErrorToResponse(final HttpServletResponse response, final ApiErrorResponse apiErrorResponse) throws IOException {
        response.setContentType("application/json");
        response.setStatus(401);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(apiErrorResponse));
        out.flush();
    }
}

