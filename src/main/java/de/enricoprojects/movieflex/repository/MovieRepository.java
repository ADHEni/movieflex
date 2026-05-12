package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    List<Movie> findByGenres(Set<Genre> genres);


}
