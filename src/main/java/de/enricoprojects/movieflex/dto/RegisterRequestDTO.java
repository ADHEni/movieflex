package de.enricoprojects.movieflex.dto;

public record RegisterRequestDTO(

        String username,
        String password,
        String email

) {
}
