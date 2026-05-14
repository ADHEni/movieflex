package de.enricoprojects.movieflex.dto;

import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MovieAllInformationDTO {


    private String title;

    private String image_url;

    private String releaseYear;

    private String description;

    private int duration;

    private float rating;

    private Set<String> actorList;

    private Set<String> genreList;

    public MovieAllInformationDTO(String title, String image_url, String releaseYear,String description, int duration, float rating, Set<String> actorList, Set<String> genreList) {
        this.title = title;
        this.image_url = image_url;
        this.releaseYear = releaseYear;
        this.description = description;
        this.duration = duration;
        this.rating = rating;
        this.actorList = actorList;
        this.genreList = genreList;
    }

    public static MovieAllInformationDTO fromMovie(Movie movie) {

        return new MovieAllInformationDTO(

                movie.getTitle(),
                movie.getImage_url(),
                movie.getReleaseYear(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getRating(),
                movie.getActors()
                        .stream()
                        .map(actor -> actor.getFirstName() + " " + actor.getLastName())
                        .collect(Collectors.toSet()),
                movie.getGenres()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.toSet())

        );


    }


}
