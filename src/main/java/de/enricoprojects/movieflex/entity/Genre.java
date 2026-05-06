package de.enricoprojects.movieflex.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Data
@Table(name = "Genre")
@Entity
public class Genre {

    @Id
    @Column(name = "genre_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();
}
