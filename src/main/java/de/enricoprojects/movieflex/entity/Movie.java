package de.enricoprojects.movieflex.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Movie")
public class Movie {

    @Id
    @Column(name = "movie_id")
    private int movie_id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "duration")
    private int duration;

    @Column(name = "release_year")
    private String release_year;

    @Column(name = "rating")
    private float rating;

    @ManyToMany
    @JoinTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn (name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name="movie_actor", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<>();

}
