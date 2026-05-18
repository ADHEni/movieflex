package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.*;
import de.enricoprojects.movieflex.exception.MovieNotFoundException;
import de.enricoprojects.movieflex.service.MovieRatingService;
import de.enricoprojects.movieflex.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class MovieController {

    MovieService movieService;
    MovieRatingService movieRatingService;


    public MovieController(MovieService movieService, MovieRatingService movieRatingService) {

        this.movieService = movieService;
        this.movieRatingService = movieRatingService;


    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieSummaryDTO>> movies() throws Exception {
        return ResponseEntity.ok(movieService.findAllMovies());
    }

    @GetMapping("/movies/{movieName}")
    public ResponseEntity<MovieAllInformationDTO> movieByName(@PathVariable String movieName) throws MovieNotFoundException {

        return ResponseEntity.ok(movieService.getMovieByName(movieName));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {

        return ResponseEntity.ok(movieService.searchMovies(title, genre));


    }

    @PostMapping("/movies")
    public ResponseEntity<MovieSummaryDTO> createMovie(@RequestBody MovieCreateRequestDTO movieCreateRequestDTO) throws MovieNotFoundException {

       return ResponseEntity.ok(movieService.createMovie(movieCreateRequestDTO));

    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) throws MovieNotFoundException {

        movieService.deleteMovie(movieId);

        return  ResponseEntity.noContent().build();



    }

    @PutMapping("/movies/{movieId}/rating")
    public ResponseEntity<MovieRatingResponseDTO> rateMovie(
            @PathVariable Long movieId,
            @Valid @RequestBody MovieRatingRequestDTO movieRatingRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails)
    {

        MovieRatingResponseDTO response = movieRatingService.rateMovie(

                movieId,
                movieRatingRequestDTO,
                userDetails.getUsername()

        );

        return ResponseEntity.ok(response);


    }
}
