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



}
