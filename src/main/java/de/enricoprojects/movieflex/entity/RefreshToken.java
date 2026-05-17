package de.enricoprojects.movieflex.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class RefreshToken {
    @Id
    private Long id;

    private String tokenHash;

    private Instant expiresAt;

    private boolean revoked;


    @ManyToOne
    private User user;


}
