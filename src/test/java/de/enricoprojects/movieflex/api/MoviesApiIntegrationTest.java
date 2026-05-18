package de.enricoprojects.movieflex.api;

import de.enricoprojects.movieflex.dto.ActorCreateRequestDTO;
import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.MovieCreateRequestDTO;
import de.enricoprojects.movieflex.entity.Genre;
import de.enricoprojects.movieflex.entity.Movie;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.GenreRepository;
import de.enricoprojects.movieflex.repository.MovieRepository;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MoviesApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldReturnMovies() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("The Matrix"))
                .andExpect(jsonPath("$[0].image_url").value("/images/matrix.jpg"))
                .andExpect(jsonPath("$[0].releaseYear").value("1999"))
                .andExpect(jsonPath("$[1].title").value("Inception"))
                .andExpect(jsonPath("$[2].title").value("Parasite"));
    }

    @Test
    void shouldReturnMovieByName() throws Exception {

        mockMvc.perform(get("/api/movies/{movieName}","Parasite"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Parasite"));


    }

    @Test
    void shouldReturnMoviesByTitleSearch() throws Exception {

        mockMvc.perform(get("/api/movies/search?title=Para"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Parasite"));


    }

    @Test
    void shouldReturnMoviesByGenreSearch() throws Exception {

        mockMvc.perform(get("/api/movies/search?genre=sci-fi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"))
                .andExpect(jsonPath("$[1].title").value("Inception"));


    }

    @Test
    void shouldNotAllowProtectedEndpointWithoutLogin() throws Exception {
        mockMvc.perform(post("/api/movies"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void searchForNonExistingMoviesShouldReturn404() throws Exception {

        mockMvc.perform(get("/api/movies/Star Wars"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MOVIE_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Movie Star Wars not found"))
                .andExpect(jsonPath("$.timestamp").exists());


    }

    @Test
    void shouldReturnEmptyListWhenTitleSearchHasNoResults() throws Exception {

        mockMvc.perform(get("/api/movies/search")
                        .param("title", "Star Wars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    void adminCanCreateMovieTestAndValidateExistence()  throws Exception {

        //creation of an admin user
        User admin = new User();
        admin.setUsername("admin" + UUID.randomUUID().toString().substring(0, 5));
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        //login of the admin user

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(admin.getUsername(), "admin");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();


        String loginResponseBody = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(loginResponseBody).get("accessToken").asText();

        //create a new Movie as an Admin

        MovieCreateRequestDTO movieCreateRequestDTO =
                new MovieCreateRequestDTO(
                        "Interstellar",
                        "A sci-fi movie about space, time and survival.",
                        "/images/interstellar.jpg",
                        169,
                        "2014",
                        Set.of("Sci-Fi", "Drama", "Adventure"),
                        Set.of(
                                new ActorCreateRequestDTO("Matthew", "McConaughey"),
                                new ActorCreateRequestDTO("Anne", "Hathaway")
                        )
                );

        //post /api/movies test to create the movie
        mockMvc.perform(post("/api/movies")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieCreateRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());


        System.out.println(genreRepository.count());
        assertThat(genreRepository.countByNameIgnoreCase("Sci-Fi")).isEqualTo(1);
        assertThat(genreRepository.countByNameIgnoreCase("Drama")).isEqualTo(1);
        assertThat(genreRepository.countByNameIgnoreCase("Adventure")).isEqualTo(1);

        Movie savedMovie = movieRepository.findByTitle("Interstellar")
                .orElseThrow();

        assertThat(savedMovie.getGenres()).hasSize(3);
        assertThat(savedMovie.getActors()).hasSize(2);

    }



}
