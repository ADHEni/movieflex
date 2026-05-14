package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.MovieAllInformationDTO;
import de.enricoprojects.movieflex.dto.MovieSummaryDTO;
import de.enricoprojects.movieflex.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class MovieController {

    MovieService movieService;


    public MovieController(MovieService movieService) {

        this.movieService = movieService;


    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieSummaryDTO>> movies() throws Exception {
        return ResponseEntity.ok(movieService.findAllMovies());
    }

    @GetMapping("/movies/{movieName}")
    public ResponseEntity<MovieAllInformationDTO> movieByName(@PathVariable String movieName) throws Exception {

        return ResponseEntity.ok(movieService.getMovieByName(movieName));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {


        return ResponseEntity.ok(movieService.searchMovies(title, genre));
        //TODO extend more filter parameters

    }



}
