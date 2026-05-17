package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.AuthResponseDTO;
import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.RegisterRequestDTO;
import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.exception.EmailAlreadyExistsException;
import de.enricoprojects.movieflex.exception.InvalidCredentialsException;
import de.enricoprojects.movieflex.exception.UsernameAlreadyExistsException;
import de.enricoprojects.movieflex.repository.UserRepository;
import de.enricoprojects.movieflex.security.SecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTService jWTService;
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, SecurityConfig passwordEncoder, JWTService jWTService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder.passwordEncoder();
        this.jWTService = jWTService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserSummaryDTO registerUser(RegisterRequestDTO registerRequestDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {


        if(userRepository.existsByUsername(registerRequestDTO.username())) {

            throw new UsernameAlreadyExistsException(registerRequestDTO.username());

        }

        if(userRepository.existsByEmail(registerRequestDTO.email())) {

            throw new EmailAlreadyExistsException(registerRequestDTO.email());

        }

        Timestamp timestamp = Timestamp.from(Instant.now());

        User user = new User();
        user.setUsername(registerRequestDTO.username());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        user.setEmail(registerRequestDTO.email());
        user.setRole("USER");
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);
        User savedUser = userRepository.save(user);




        return UserSummaryDTO.from(savedUser);

    }


    public AuthResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {


        Optional<User> user = userRepository.findByUsername(loginRequestDTO.usernamen());

        if(user.isEmpty()) {

            throw new InvalidCredentialsException();

        }

        boolean passwordIsValid = passwordEncoder.matches(loginRequestDTO.password(), user.get().getPassword());

        if(!passwordIsValid) {

            throw new InvalidCredentialsException();

        }

        String accessToken = jWTService.createAccessToken(user.get());
        String rawRefreshToken = refreshTokenService.createRefreshToken(user.get());

        return new AuthResponseDTO(accessToken,"Bearer",rawRefreshToken,UserSummaryDTO.from(user.get()));

    }




}
