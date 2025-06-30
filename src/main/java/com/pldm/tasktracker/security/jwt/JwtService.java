package com.pldm.tasktracker.security.jwt;

import com.pldm.tasktracker.dto.JwtAuthenticationDto;
import com.pldm.tasktracker.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    @Value("6aeecf726dfd4a0913d6ffbda9c505422d172875a4f2dc0ed1c46017cfe2d47e")
    private String jwtSecret;

    public JwtAuthenticationDto generateAuthToken(String username, Role role){

        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username, role));
        jwtDto.setRefreshToken(generateRefreshToken(username,role));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken, Role role){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username, role));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }
    public boolean validateJwtToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch(ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        }catch (UnsupportedJwtException expEx){
            LOGGER.error("Unsupported JwtException", expEx);
        }catch(MalformedJwtException expEx){
            LOGGER.error("Malformed JwtException", expEx);
        }catch(SecurityException expEx){
            LOGGER.error("Security Exception", expEx);
        }catch(Exception expEx){
            LOGGER.error("Invalid Token", expEx);
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    public String generateJwtToken(String username, Role role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        Date date= Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .subject(username)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }
    public String generateRefreshToken(String username, Role role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        Date date= Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(claims)
                .subject(username)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }
    public SecretKey getSingInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
