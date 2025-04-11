package it.unipd.bookly.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;

public class JWTUtil {

    // Secret key used for signing tokens (replace with environment variable in production)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration (e.g. 1 hour)
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the user's username
     * @return a JWT token string
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Validates a token and returns the username (subject) if valid.
     *
     * @param token the JWT token string
     * @return the username inside the token, or null if invalid
     */
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(SECRET_KEY)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null; // token is invalid or expired
        }
    }

    /**
     * Checks whether a token is valid.
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public static boolean isTokenValid(String token) {
        return validateToken(token) != null;
    }
}
