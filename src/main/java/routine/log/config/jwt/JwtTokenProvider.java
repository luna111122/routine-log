package routine.log.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire-seconds}")
    private long expireSeconds;

    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username){
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)

                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expireSeconds)))
                .signWith(key)
                .compact();
    }

    public boolean validate(String token){
        try{
            parseClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public String getUserId(String token) {
        return parseClaims(token).get("userId", String.class);
    }

    public String getUsername(String token){
        return parseClaims(token).getSubject();
    }
    public long getExpirationEpochMillis(String token) {
        Date exp = parseClaims(token).getExpiration();
        return exp.getTime();
    }

    private Claims parseClaims(String token) {
        return (Claims) Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }



}
