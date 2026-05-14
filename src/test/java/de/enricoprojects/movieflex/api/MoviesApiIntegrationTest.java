package de.enricoprojects.movieflex.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class MoviesApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
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
    @WithMockUser
    void shouldReturnMovieByName() throws Exception {

        mockMvc.perform(get("/api/movies/{movieName}","Parasite"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Parasite"));


    }

    @Test
    @WithMockUser
    void shouldReturnMoviesByTitleSearch() throws Exception {

        mockMvc.perform(get("/api/movies/search?title=Para"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Parasite"));


    }

    @Test
    @WithMockUser
    void shouldReturnMoviesByGenreSearch() throws Exception {

        mockMvc.perform(get("/api/movies/search?genre=sci-fi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"))
                .andExpect(jsonPath("$[1].title").value("Inception"));


    }



}
