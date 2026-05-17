package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.*;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.UserRepository;
import de.enricoprojects.movieflex.service.AuthService;
import de.enricoprojects.movieflex.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {

        this.authService = authService;
        this.refreshTokenService = refreshTokenService;

    }

    @PostMapping("/register")
    public ResponseEntity<UserSummaryDTO> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {

        UserSummaryDTO createdUser = authService.registerUser(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(createdUser);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {


        return ResponseEntity.ok(authService.loginUser(loginRequestDTO));


    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

            return ResponseEntity.ok(refreshTokenService.refresh(refreshTokenRequestDTO.getRefreshToken()));


    }



}
