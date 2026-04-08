package com.fiap.pj.infra.helpers;

import com.fiap.pj.infra.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final String SCOPE = "scope";
    private static final String TIPO = "tipo";
    public static final String USER_TYPE = "USER";
    public static final String CLIENTE_TYPE = "CLIENTE";
    private static final int EXPIRATION_USER_HOURS = 10;

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTipo(String token) {
        return extractClaim(token, claims -> claims.get(TIPO, String.class));
    }

    public String extractScope(String token) {
        return extractClaim(token, claims -> claims.get(SCOPE, String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }


    public boolean validateToken(String token, String expectedSubject) {
        String subject = extractSubject(token);
        return subject.equals(expectedSubject) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateUserToken(UserDetailsImpl user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tipo", USER_TYPE);
        claims.put("nome", user.getNomeCompleto());
        claims.put("usuario_id", user.getId());
        claims.put("roles", user.getAuthorities()
                .stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .toList()
        );

        return createToken(claims, user.getUsername(), EXPIRATION_USER_HOURS);
    }

    private String createToken(
            Map<String, Object> claims,
            String subject,
            int expirationHours
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(
                                Instant.now().plus(expirationHours, ChronoUnit.HOURS)
                        )
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        return parser.parseClaimsJws(token).getBody();
    }

}
