package de.enricoprojects.movieflex.dto;

import de.enricoprojects.movieflex.entity.Actor;
import de.enricoprojects.movieflex.entity.Genre;
import lombok.Data;

import java.util.List;

@Data
public class MovieResponseDTO {


    private String title;

    private String description;

    private String image_url;

    private String releaseYear;

    private float rating;

    private int duration;

    private List<Actor> actorsList;

    private List<Genre> genresList;


}
