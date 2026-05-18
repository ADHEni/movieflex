package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.MovieRatingRequestDTO;
import de.enricoprojects.movieflex.dto.MovieRatingResponseDTO;
import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.entity.MovieRating;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.exception.InvalidCredentialsException;
import de.enricoprojects.movieflex.exception.MovieNotFoundException;
import de.enricoprojects.movieflex.repository.MovieRatingRepository;
import de.enricoprojects.movieflex.repository.MovieRepository;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovieRatingService {

    private final MovieRepository movieRepository;
    private MovieRatingRepository movieRatingRepository;
    private UserRepository userRepository;

    public MovieRatingService(MovieRatingRepository movieRatingRepository, UserRepository userRepository, MovieRepository movieRepository) {

        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public MovieRatingResponseDTO rateMovie(Long movieId, MovieRatingRequestDTO movieRatingRequestDTO, String userDetails) {

        User user =  userRepository.findByUsername(userDetails).orElseThrow(InvalidCredentialsException::new);
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        MovieRating movieRating = movieRatingRepository
                .findByUserAndMovie(user,movie)
                .orElseGet(() -> {

                    MovieRating newMovieRating = new MovieRating();
                    newMovieRating.setUser(user);
                    newMovieRating.setMovie(movie);
                    newMovieRating.setCreatedAt(LocalDateTime.now());
                    return newMovieRating;

                });


        movieRating.setRatingValue(movieRatingRequestDTO.ratingValue());
        movieRating.setUpdatedAt(LocalDateTime.now());

        MovieRating savedMovieRating = movieRatingRepository.save(movieRating);
        movieRatingRepository.save(movieRating);

        Double averageRating = Optional.of(movieRatingRepository.findAverageRatingByMovieMovie_id(movieId)).orElse(0.0);
        Long countRating = movieRatingRepository.countRatingByMovieMovie_id(movieId);

        return new MovieRatingResponseDTO(savedMovieRating.getRatingValue(), averageRating, countRating);

    }





}
