package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.ActorCreateRequestDTO;
import de.enricoprojects.movieflex.dto.MovieAllInformationDTO;
import de.enricoprojects.movieflex.dto.MovieCreateRequestDTO;
import de.enricoprojects.movieflex.dto.MovieSummaryDTO;
import de.enricoprojects.movieflex.entity.Actor;
import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.exception.MovieAlreadyExistsException;
import de.enricoprojects.movieflex.exception.MovieNotFoundException;
import de.enricoprojects.movieflex.repository.ActorRepository;
import de.enricoprojects.movieflex.repository.GenreRepository;
import de.enricoprojects.movieflex.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {


    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }


    public List<MovieSummaryDTO> findAllMovies() throws Exception{

        return movieRepository.findAll()
                .stream()
                .map(MovieSummaryDTO::from)
                .toList();


    }

    public MovieAllInformationDTO getMovieByName(String title) throws MovieNotFoundException {

        return movieRepository.findByTitle(title)
                .map(MovieAllInformationDTO::fromMovie)
                .orElseThrow(() -> new MovieNotFoundException(title));


    }

    public List<MovieSummaryDTO> searchMovies(String title, String genre) {

        title = MovieService.normalize(title);
        genre = MovieService.normalize(genre);


        return movieRepository.findBySearchParameters(title, genre)
                .stream()
                .map(MovieSummaryDTO::from)
                .toList();


    }


    @Transactional
    public MovieSummaryDTO createMovie(MovieCreateRequestDTO movieCreateRequestDTO) throws MovieNotFoundException {

       Optional<Movie> movie = movieRepository.findByTitle(movieCreateRequestDTO.title());

       if(movie.isPresent()) {

           throw new MovieAlreadyExistsException("Movie already Exists");

       }

       Set<Genre> genres = movieCreateRequestDTO.genres().stream().map(this::findOrCreateGenre).collect(Collectors.toSet());
       Set<Actor> actors = movieCreateRequestDTO.actors().stream().map(this::findOrCreateActor).collect(Collectors.toSet());

       Movie newMovie = new Movie();

       newMovie.setTitle(movieCreateRequestDTO.title());
       newMovie.setImage_url(movieCreateRequestDTO.imageUrl());
       newMovie.setDescription(movieCreateRequestDTO.description());
       newMovie.setDuration(movieCreateRequestDTO.duration());
       newMovie.setReleaseYear(movieCreateRequestDTO.releaseYear());
       newMovie.setGenres(genres);
       newMovie.setActors(actors);

       Movie savedMovie = movieRepository.save(newMovie);
       return  MovieSummaryDTO.from(savedMovie);
    }



    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }


    private Genre findOrCreateGenre(String genreName) throws MovieNotFoundException {

        String normalizedGenre = normalize(genreName);

        return genreRepository.findByNameIgnoreCase(genreName).orElseGet(() ->
        {
            Genre genre = new Genre();
            genre.setName(normalizedGenre);
            return genreRepository.save(genre);

        });
    }

    private Actor findOrCreateActor(ActorCreateRequestDTO actorCreateRequestDTO) {

        String firstName = actorCreateRequestDTO.firstName();
        String lastName = actorCreateRequestDTO.lastName();

        return actorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName,lastName).orElseGet(() -> {

            Actor actor = new Actor();

            actor.setFirstName(firstName);
            actor.setLastName(lastName);
            return actorRepository.save(actor);

        });


    }


}
