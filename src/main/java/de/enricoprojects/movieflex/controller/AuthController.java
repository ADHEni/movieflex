package de.enricoprojects.movieflex.controller;
import de.enricoprojects.movieflex.dto.*;
import de.enricoprojects.movieflex.service.AuthService;
import de.enricoprojects.movieflex.service.RateLimitService;
import de.enricoprojects.movieflex.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    private final RateLimitService rateLimitService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, RateLimitService rateLimitService) {

        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserSummaryDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO, HttpServletRequest request) {
        rateLimitService.checkRateLimit(RateLimitService.RateLimitAction.REGISTER, request.getRequestURI());
        UserSummaryDTO createdUser = authService.registerUser(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(createdUser);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO,HttpServletRequest request ) {

        rateLimitService.checkRateLimit(RateLimitService.RateLimitAction.LOGIN, request.getRequestURI());

        return ResponseEntity.ok(authService.loginUser(loginRequestDTO));


    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO, HttpServletRequest request) {

        rateLimitService.checkRateLimit(RateLimitService.RateLimitAction.REFRESH, request.getRequestURI());
        return ResponseEntity.ok(refreshTokenService.refresh(refreshTokenRequestDTO.getRefreshToken()));


    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDTO,HttpServletRequest request) {

        rateLimitService.checkRateLimit(RateLimitService.RateLimitAction.LOGOUT, request.getRemoteAddr());
        refreshTokenService.revokeRefreshToken(logoutRequestDTO.refreshToken());

        return ResponseEntity.noContent().build();

    }



}
