package trinh_be.utils;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@UtilityClass
public class JwtUtils {

    private static final String SECRET;

    private static final Key key;

    static {
        Dotenv dotenv = Dotenv.load();
        SECRET = dotenv.get("JWT_SECRET");
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 3)) // 3 days
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidToken(String token, String email) {
        String jwtEmail = extractEmail(token);
        return !isExpired(token) && jwtEmail.equals(email);
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return false;
        } catch (JwtException e) {
            return true;
        }
    }
}
