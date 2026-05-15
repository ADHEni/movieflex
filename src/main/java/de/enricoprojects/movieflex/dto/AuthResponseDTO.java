package de.enricoprojects.movieflex.dto;

public record AuthResponseDTO(

        String accessToken,
        String tokenType,
        UserSummaryDTO user


) {
}
