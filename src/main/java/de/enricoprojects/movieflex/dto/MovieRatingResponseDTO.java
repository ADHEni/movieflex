package de.enricoprojects.movieflex.dto;

public record MovieRatingResponseDTO(

        Integer ratingValue,
        Double averageRating,
        Long ratingCount


) {
}
