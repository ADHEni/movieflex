package de.enricoprojects.movieflex.exception;

import de.enricoprojects.movieflex.dto.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleMovieNotFoundException(MovieNotFoundException ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                404,
                "MOVIE_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                409,
                "USERNAME_ALREADY_EXISTS",
                ex.getMessage(),
                LocalDateTime.now()


        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);


    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                409,
                "EMAIL_ALREADY_EXISTS",
                ex.getMessage(),
                LocalDateTime.now()


        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);


    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                401,
                "INVALID_CREDENTIALS",
                ex.getMessage(),
                LocalDateTime.now()


        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);


    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidRefreshToken(InvalidRefreshToken ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                401,
                "INVALID_REFRESH_TOKEN",
                ex.getMessage(),
                LocalDateTime.now()

        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);


    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorDTO> handleRateLimitExceededException(RateLimitExceededException ex) {

        ApiErrorDTO error = new ApiErrorDTO(

                HttpStatus.TOO_MANY_REQUESTS.value(),
                "RATE_LIMIT_EXCEEDED",
                ex.getMessage(),
                LocalDateTime.now()

        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);


    }



}
