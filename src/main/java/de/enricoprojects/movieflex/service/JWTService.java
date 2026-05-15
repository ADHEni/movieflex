package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.config.JWTConfig;
import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {


    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public JWTService(JwtEncoder jwtEncoder,  @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {


        this.jwtEncoder = jwtEncoder;
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



}
