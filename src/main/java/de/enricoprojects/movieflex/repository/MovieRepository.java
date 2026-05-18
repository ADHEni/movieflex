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

    Optional<Movie> findByMovie_id(Long movieId);



    @Query("""
            SELECT DISTINCT m
            FROM Movie m LEFT JOIN m.genres g
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%',:title,'%')))
            AND (:genre IS NULL OR LOWER (g.name) = LOWER(:genre))
            """)
    List<Movie> findBySearchParameters(
            @Param("title") String title,
            @Param("genre") String genre
    );


}
