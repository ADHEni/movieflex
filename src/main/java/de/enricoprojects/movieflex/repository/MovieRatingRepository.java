package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.entity.MovieRating;
import de.enricoprojects.movieflex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {



    Optional<MovieRating> findByUserAndMovie(User user, Movie movie);

    @Query(" SELECT avg(r.ratingValue) FROM MovieRating r WHERE r.movie.movie_id= :movieId")
    Double findAverageRatingByMovieMovie_id(@Param("movieId") Long movieId);

    @Query("SELECT count(r) FROM MovieRating r WHERE r.movie.movie_id = :movieId")
    Long countRatingByMovieMovie_id(@Param("movieId") Long movieId);

}

