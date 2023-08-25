package com.learning.ecommerce.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.learning.ecommerce.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${jwt.expirationMs}")
    private Integer jwtExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private Integer refreshJwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret)
                     .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT Signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token Expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT Token is Unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT Claims String is Empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateJwtToken(Authentication auth){
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder().setSubject((principal.getUsername()))
                             .setIssuedAt(new Date())
                             .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                             .signWith(SignatureAlgorithm.HS512, jwtSecret)
                             .compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret)
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();
    }

    public String generateRefreshJwtToken(Authentication auth){
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder().setSubject((principal.getUsername()))
                             .setIssuedAt(new Date())
                             .setExpiration(new Date((new Date()).getTime() + refreshJwtExpirationMs))
                             .signWith(SignatureAlgorithm.HS512, jwtSecret)
                             .compact();
    }
}
