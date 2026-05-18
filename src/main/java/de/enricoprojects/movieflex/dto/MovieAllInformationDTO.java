package de.enricoprojects.movieflex.dto;

import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MovieAllInformationDTO {

    private Long movie_id;
    private String title;
    private String description;
    private String image_url;
    private int duration;
    private String releaseYear;
    private Set<String> genres;
    private Set<String> actors;

    private Double ratingAverage;
    private Long ratingCount;

    public MovieAllInformationDTO(
            Long movie_id,
            String title,
            String description,
            String image_url,
            int duration,
            String releaseYear,
            Set<String> genres,
            Set<String> actors,
            Double ratingAverage,
            Long ratingCount
    ) {
        this.movie_id = movie_id;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.actors = actors;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;
    }

    public static MovieAllInformationDTO from(Movie movie) {
        return new MovieAllInformationDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getImage_url(),
                movie.getDuration(),
                movie.getReleaseYear(),
                movie.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toSet()),
                movie.getActors()
                        .stream()
                        .map(actor -> actor.getFirstName() + " " + actor.getLastName())
                        .collect(Collectors.toSet()),
                0.0,
                0L
        );
    }

    public static MovieAllInformationDTO from(
            Movie movie,
            Double ratingAverage,
            Long ratingCount
    ) {
        return new MovieAllInformationDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getImage_url(),
                movie.getDuration(),
                movie.getReleaseYear(),
                movie.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toSet()),
                movie.getActors()
                        .stream()
                        .map(actor -> actor.getFirstName() + " " + actor.getLastName())
                        .collect(Collectors.toSet()),
                ratingAverage,
                ratingCount
        );
    }
}
