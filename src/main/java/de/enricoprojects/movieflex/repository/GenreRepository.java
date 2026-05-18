package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByNameIgnoreCase(String name);

    long countByNameIgnoreCase(String name);


}
