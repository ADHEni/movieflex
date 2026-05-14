package de.enricoprojects.movieflex.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String title) {
        super("Movie" + title + " not found");
    }
}
