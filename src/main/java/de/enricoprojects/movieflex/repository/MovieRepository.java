package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.Actor;
import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    List<Movie> findByGenres(Set<Genre> genres);

    Optional<Movie> findByMovieId(Long movieId);



    boolean existsByTitleIgnoreCase(String title);

    List<Movie> findDistinctByTitleContainingIgnoreCase(String title);

    List<Movie> findDistinctByGenres_NameIgnoreCase(String genre);

    List<Movie> findDistinctByTitleContainingIgnoreCaseAndGenres_NameIgnoreCase(
            String title,
            String genre
    );


}
