package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.MovieSummaryDTO;
import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.exception.BadDbRequestForMovies;
import de.enricoprojects.movieflex.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class MovieController {


    MovieRepository movieRepository;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieSummaryDTO>> movies()  {
        try {
            List<Movie> allMovies = movieRepository.findAll();
            List<MovieSummaryDTO> movieSummaryDTOS = allMovies
                    .stream()
                    .map(MovieSummaryDTO::from)
                    .toList();

            return ResponseEntity.ok(movieSummaryDTOS);
        } catch (BadDbRequestForMovies e) {

            return ResponseEntity.badRequest().build();

        }
    }


}
