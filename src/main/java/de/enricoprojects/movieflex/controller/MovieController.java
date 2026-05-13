package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.MovieAllInformationDTO;
import de.enricoprojects.movieflex.dto.MovieSummaryDTO;
import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.exception.BadDbRequestForMovies;
import de.enricoprojects.movieflex.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MovieController {


    MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

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

    @GetMapping("/movies/{movieName}")
    public ResponseEntity<MovieAllInformationDTO> movieByName(@PathVariable String movieName) {

        return movieRepository.findByTitle(movieName)
                .map(MovieAllInformationDTO::fromMovie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
