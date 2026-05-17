package de.enricoprojects.movieflex.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken(String message) {
        super(message);
    }
}
