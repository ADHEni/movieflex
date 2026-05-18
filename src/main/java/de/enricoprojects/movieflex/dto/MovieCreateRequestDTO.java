package de.enricoprojects.movieflex.dto;

import java.util.Set;


public record MovieCreateRequestDTO(
        String title,
        String description,
        String imageUrl,
        Integer duration,
        String releaseYear,
        Set<String> genres,
        Set<ActorCreateRequestDTO> actors
) {}


