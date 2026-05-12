package de.enricoprojects.movieflex.exception;

public class BadDbRequestForMovies extends RuntimeException {
    public BadDbRequestForMovies(String message) {
        super(message);
    }
}
