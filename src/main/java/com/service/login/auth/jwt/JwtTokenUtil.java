package com.service.login.auth.jwt;

import com.service.login.auth.service.UserService;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final long serialVersionUID = -8341002642345284039L;

    @Autowired
    UserService userService;
    @Value("${jwt.user.token.secret}")
    private String jwtSecret;
    @Value("${jwt.token.expire.time}")
    private String jwtExpirationMs;

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("INVALID_JWT_SIGNATURE", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("INVALID_JWT", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT_EXPIRED", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT_TOKEN_UNSUPPORTED", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT_CLAIMS_EMPTY", e.getMessage());
        }

        return false;
    }

    public String generateTokenForUser(String username, String hash) {
        LocalDateTime now = LocalDateTime.now();
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("hash", hash);
        return Jwts.builder().setSubject(username).setClaims(claims).setIssuedAt(nowDate)
                .setExpiration(Date.from(now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }
}

