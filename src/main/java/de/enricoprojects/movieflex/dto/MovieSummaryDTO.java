package de.enricoprojects.movieflex.dto;

import de.enricoprojects.movieflex.entity.Actor;
import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import lombok.Data;

import java.util.List;

@Data
public class MovieSummaryDTO {


    private String title;

    private String image_url;

    private String releaseYear;

    public MovieSummaryDTO(String title, String image_url, String releaseYear) {

        this.image_url = image_url;
        this.releaseYear = releaseYear;
        this.title = title;

    }

    public static MovieSummaryDTO from(Movie movie) {

        return new MovieSummaryDTO(

                movie.getTitle(),
                movie.getImage_url(),
                movie.getRelease_year()

        );

    }


}
