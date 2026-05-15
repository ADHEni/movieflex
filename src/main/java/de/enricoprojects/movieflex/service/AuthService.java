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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTService jWTService;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jWTService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jWTService = jWTService;
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

        return new AuthResponseDTO(accessToken,"Bearer",UserSummaryDTO.from(user.get()));

    }




}
