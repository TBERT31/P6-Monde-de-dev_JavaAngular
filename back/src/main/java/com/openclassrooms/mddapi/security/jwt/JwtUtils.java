package com.openclassrooms.mddapi.security.jwt;

import java.util.Date;

import com.google.gson.Gson;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;

/**
 * Utilitaires pour travailler avec JSON Web Tokens (JWT).
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final Gson gson = new Gson();

    // Secret pour signer le JWT
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    // Durée d'expiration du JWT en millisecondes
    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Génère un JWT à partir des informations d'authentification.
     * @param authentication les informations d'authentification.
     * @return le JWT généré.
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Récupère le nom d'utilisateur à partir d'un JWT.
     * @param token le JWT.
     * @return le nom d'utilisateur.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valide un JWT.
     * @param authToken le JWT à valider.
     * @return vrai si le JWT est valide, faux sinon.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Convertit un objet en JSON.
     * @param obj l'objet à convertir.
     * @return la représentation JSON de l'objet.
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
