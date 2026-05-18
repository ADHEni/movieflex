package de.enricoprojects.movieflex.dto;
import de.enricoprojects.movieflex.entity.Movie;
import lombok.Data;


@Data
public class MovieSummaryDTO {

    private Long  movie_id;

    private String title;

    private String image_url;

    private String releaseYear;

    private Double ratingAverage;
    private Long ratingCount;

    public MovieSummaryDTO(Long movie_id, String title, String image_url, String releaseYear,  Double ratingAverage, Long ratingCount) {
        this.movie_id = movie_id;
        this.image_url = image_url;
        this.releaseYear = releaseYear;
        this.title = title;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;

    }

    public static MovieSummaryDTO from(Movie movie) {

        return new MovieSummaryDTO(
                movie.getMovie_id(),
                movie.getTitle(),
                movie.getImage_url(),
                movie.getReleaseYear(),
                0.0,
                0L

        );

    }
    public static MovieSummaryDTO from(
            Movie movie,
            Double ratingAverage,
            Long ratingCount
    ) {
        return new MovieSummaryDTO(
                movie.getMovie_id(),
                movie.getTitle(),
                movie.getImage_url(),
                movie.getReleaseYear(),
                ratingAverage,
                ratingCount
        );
    }


}
