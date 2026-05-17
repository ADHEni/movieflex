package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.config.TokenHashConfig;
import de.enricoprojects.movieflex.dto.AuthResponseDTO;
import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.RefreshToken;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.exception.InvalidRefreshToken;
import de.enricoprojects.movieflex.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;
    private final UserService userService;

    @Value("${app.jwt.expiration-refresh-token-days}")
    private long refreshExpirationDays;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JWTService jwtService, UserService userService) {

        this.refreshTokenRepository = refreshTokenRepository;

        this.jwtService = jwtService;
        this.userService = userService;
    }

    public AuthResponseDTO refresh(String rawRefreshToken) {

        String tokenHash = hashToken(rawRefreshToken);

        Optional<RefreshToken> foundRefrechToken = Optional.ofNullable(refreshTokenRepository
                .findByTokenHashAndRevokedFalse(tokenHash).orElseThrow(() -> new InvalidRefreshToken("Invalid refresh token")));

        if(foundRefrechToken.isPresent()) {

            if(foundRefrechToken.get().getExpiresAt().isBefore(Instant.now())) {

                foundRefrechToken.get().setRevoked(true);
                refreshTokenRepository.save(foundRefrechToken.get());
                throw new InvalidRefreshToken("Refresh token expired");


            }

            User user =  foundRefrechToken.get().getUser();

            foundRefrechToken.get().setRevoked(true);
            refreshTokenRepository.save(foundRefrechToken.get());


            String newAccesToken = jwtService.createAccessToken(user);
            UserSummaryDTO userDto = userService.getUserSummaryDTO(user.getUsername());
            String newRefreshToken = createRefreshToken(user);

            return new AuthResponseDTO(

                    newAccesToken,
                    "Bearer",
                    newRefreshToken,
                    userDto
            );


        }

        throw new InvalidRefreshToken("Invalid refresh token");

    }

    public String createRefreshToken(User user) {
        String rawToken = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    private String hashToken(String rawRefreshToken) {

        return TokenHashConfig.hashToken(rawRefreshToken);


    }


}
