package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.AuthResponseDTO;
import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.RegisterRequestDTO;
import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.UserRepository;
import de.enricoprojects.movieflex.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService;

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



}
