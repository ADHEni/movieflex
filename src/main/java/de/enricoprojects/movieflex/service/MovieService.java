package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.MovieAllInformationDTO;
import de.enricoprojects.movieflex.dto.MovieSummaryDTO;
import de.enricoprojects.movieflex.exception.MovieNotFoundException;
import de.enricoprojects.movieflex.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {


    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    public List<MovieSummaryDTO> findAllMovies() throws Exception{

        return movieRepository.findAll()
                .stream()
                .map(MovieSummaryDTO::from)
                .toList();


    }

    public MovieAllInformationDTO getMovieByName(String title) {

        return movieRepository.findByTitle(title)
                .map(MovieAllInformationDTO::fromMovie)
                .orElseThrow(() -> new MovieNotFoundException(title));


    }

    public List<MovieSummaryDTO> searchMovies(String title, String genre) {

        title = MovieService.normalize(title);
        genre = MovieService.normalize(genre);


        return movieRepository.findBySearchParameters(title, genre)
                .stream()
                .map(MovieSummaryDTO::from)
                .toList();


    }



    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }


}
