package de.enricoprojects.movieflex.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "Actor")
@Entity
public class Actor {

    @Id
    @Column(name = "actor_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();

}
