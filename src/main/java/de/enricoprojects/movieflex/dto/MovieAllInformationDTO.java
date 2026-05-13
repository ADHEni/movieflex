package de.enricoprojects.movieflex.dto;


import de.enricoprojects.movieflex.entity.Actor;
import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MovieAllInformationDTO {


    private String title;

    private String image_url;

    private String releaseYear;

    private String duration;


    private float rating;

    private Set<String> actorList;

    private Set<String> genreList;

    public MovieAllInformationDTO(String title, String image_url, String releaseYear, String duration, float rating, Set<String> actorList, Set<String> genreList) {
        this.title = title;
        this.image_url = image_url;
        this.releaseYear = releaseYear;
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
