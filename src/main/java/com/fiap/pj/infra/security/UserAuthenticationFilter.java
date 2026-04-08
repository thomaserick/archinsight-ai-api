package com.fiap.pj.infra.security;

import com.fiap.pj.infra.helpers.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.fiap.pj.infra.helpers.JwtUtil.CLIENTE_TYPE;
import static com.fiap.pj.infra.helpers.JwtUtil.USER_TYPE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
@Slf4j
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";


    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String jwt = extractToken(request);

        if (isNull(jwt) || nonNull(SecurityContextHolder.getContext().getAuthentication() )) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String subject = jwtUtil.extractSubject(jwt);
            String tipo = jwtUtil.extractTipo(jwt);

            if (USER_TYPE.equals(tipo)) {
                authenticateUser(subject, jwt, request);
            } else if (CLIENTE_TYPE.equals(tipo)) {
                authenticateCliente(subject, jwt, request);
            } else {
                log.warn("Tipo de token desconhecido");
            }

        } catch (ExpiredJwtException e) {
            log.warn("JWT token está expirado");
        } catch (Exception e) {
            log.warn("Não foi possível processar o JWT", e);
        }

        chain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (nonNull(header) && header.startsWith(BEARER)) {
            return header.substring(BEARER.length());
        }
        return null;
    }

    /**
     * Fluxo – usuários internos
     */
    private void authenticateUser(
            String username,
            String jwt,
            HttpServletRequest request
    ) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(jwt, userDetails.getUsername())) {
            return;
        }

        setAuthentication(userDetails, (List<SimpleGrantedAuthority>) userDetails.getAuthorities(), request);
    }

    /**
     * Fluxo SERVERLESS – cliente por CPF
     */

    private void authenticateCliente(
            String cpf,
            String jwt,
            HttpServletRequest request
    ) {
        if (!jwtUtil.validateToken(jwt, cpf)) {
            return;
        }

        String scope = jwtUtil.extractScope(jwt);
        setAuthentication(cpf, List.of(new SimpleGrantedAuthority(scope)), request);
    }


    private void setAuthentication(Object principal, List<SimpleGrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authorities
                );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
