package bibliomar.bibliomarserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    String jwtSecret;
    Algorithm algorithmHS;
    JWTVerifier jwtVerifier;

    private final String issuer = "bibliomar";

    private final Long maxAgeInSeconds = 259200L;
    private final Long maxPasswordlessAgeInSeconds = 3600L;

    @PostConstruct
    public void configureJwt() {
        algorithmHS = Algorithm.HMAC512(jwtSecret);
        jwtVerifier = JWT.require(algorithmHS).withIssuer(issuer).build();
    }

    public DecodedJWT getJwtFromHeader(HttpServletRequest request) throws JWTVerificationException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            DecodedJWT jwt = jwtVerifier.verify(token);
            return jwt;
        }

        throw new JWTVerificationException("Invalid token");
    }

    public String getUsernameFromJwtToken(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(maxAgeInSeconds);
        Instant expiresAtInstant = expiresAt.atZone(ZoneId.systemDefault()).toInstant();
        return JWT.create()
                .withIssuer(this.issuer)
                .withSubject(username)
                .withExpiresAt(expiresAtInstant)
                .sign(algorithmHS);
    }

    public String generatePasswordlessToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(maxPasswordlessAgeInSeconds);
        Instant expiresAtInstant = expiresAt.atZone(ZoneId.systemDefault()).toInstant();
        return JWT.create()
                .withIssuer(this.issuer)
                .withSubject(username)
                .withExpiresAt(expiresAtInstant)
                .sign(algorithmHS);
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return jwtVerifier.verify(token);
    }


}
