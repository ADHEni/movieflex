package de.enricoprojects.movieflex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "movie_ratings", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id","movie_id"})})
public class MovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column(nullable = false)
    private int ratingValue;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
