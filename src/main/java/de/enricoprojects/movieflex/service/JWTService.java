package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.config.JWTConfig;
import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {


    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final long expirationMinutes;

    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {


        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.expirationMinutes = expirationMinutes;
    }




    public String createAccessToken(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("movieflex")
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();


        return jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();

    }

    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();

    }


    public boolean isTokenValid(String token, UserDetails user) {

        String username = extractUsername(token);

        return username.equals(user.getUsername()) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {

        Jwt jwt = jwtDecoder.decode(token);

        Instant expirationDate = jwt.getExpiresAt();

        return expirationDate == null || expirationDate.isBefore(Instant.now());


    }


}
