package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.model.User;
import academy.scalefocus.timeOffManagement.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static academy.scalefocus.timeOffManagement.utils.TokenServiceProperties.*;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {
    @Value(SECRET_PROPERTY)
    private String JWT_SECRET;

    @Override
    public String generateToken(User user) {
        Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String compactTokenString = Jwts.builder()
                .claim(BEARER_ID_FIELD, user.getId())
                .claim(BEARER_SUBJECT_FIELD, user.getUsername())
                .claim(BEARER_ADMIN_FIELD, user.getIsAdmin())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return compactTokenString;
    }

    /**
     * @param token - the compact token
     */
    @Override
    public UserPrincipal parseToken(String token) {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        String username = jwsClaims.getBody()
                .getSubject();
        Long userId = jwsClaims.getBody()
                .get(BEARER_ID_FIELD, Long.class);
        boolean isAdmin = jwsClaims.getBody().get(BEARER_ADMIN_FIELD, Boolean.class);

        return new UserPrincipal(userId, username, isAdmin);
    }
}
