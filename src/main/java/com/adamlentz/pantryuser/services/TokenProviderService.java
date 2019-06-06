package com.adamlentz.pantryuser.services;

import com.adamlentz.pantryuser.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class TokenProviderService {

    @Value("${tokenauthentication.tokenexpiration}")
    public static final int DAYS = 10;

    public static final Key key =  Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(DAYS);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(
                        ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key).compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        }
        catch (MalformedJwtException ex)
        {
            log.error("Invalid JWT token");
        }
        catch (ExpiredJwtException ex)
        {
            log.error("Expired JWT token");
        }
        catch (UnsupportedJwtException ex)
        {
            log.error("Unsupported JWT token");
        }
        catch (IllegalArgumentException ex)
        {
            log.error("JWT claims string is empty");
        }
        return false;

    }
}
