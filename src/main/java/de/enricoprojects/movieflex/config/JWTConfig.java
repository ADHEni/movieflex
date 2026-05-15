package de.enricoprojects.movieflex.config;


import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JWTConfig {


    @Value("${app.jwt.secret}")
    private String jwtSecret;


    @Bean
    public SecretKey jwtSecretKey() {

        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return new SecretKeySpec(keyBytes, "HmacSHA256");

    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecurityKey) {

        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecurityKey));


    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecurityKey) {

        return NimbusJwtDecoder
                .withSecretKey(jwtSecurityKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();


    }



}
