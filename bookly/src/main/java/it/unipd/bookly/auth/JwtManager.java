package it.unipd.bookly.auth;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

public class JwtManager {

    private static final String CLAIM_USERNAME = "username";
    private static final SecretKey KEY = HS256.key().build();
    private static final TemporalAmount TOKEN_VALIDITY = Duration.ofHours(4L);

    /**
     * Creates a JWT token for the given subject (user ID) and username.
     *
     * @param subject  the subject (typically user ID or email)
     * @param username the username to include as a claim
     * @return a signed JWT token
     */
    public static String createToken(final String subject, final String username) {
        final Instant now = Instant.now();
        final Date expiryDate = Date.from(now.plus(TOKEN_VALIDITY));
        return Jwts.builder()
                .subject(subject)
                .claim(CLAIM_USERNAME, username)
                .expiration(expiryDate)
                .issuedAt(Date.from(now))
                .signWith(KEY)
                .compact();
    }

    /**
     * Validates and parses a JWT token.
     *
     * @param compactToken the JWT token to parse
     * @return the claims if valid, or null if the token is invalid or expired
     */
    public static Jws<Claims> parseToken(final String compactToken) {
        try {
            return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(compactToken);
        } catch (JwtException ex) {
            System.err.println("JWT parsing failed: " + ex.getMessage());
            return null;
        }
    }
}
