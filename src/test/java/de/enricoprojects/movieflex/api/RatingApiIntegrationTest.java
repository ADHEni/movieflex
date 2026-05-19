package de.enricoprojects.movieflex.api;

import de.enricoprojects.movieflex.dto.MovieRatingRequestDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.MovieRatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class RatingApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRatingRepository movieRatingRepository;



    @Test
    @Transactional
    public void loginUserAndRateAMovie() throws Exception {

        User user = createUser("testUser","testPassword","USER");

        String accessToken = loginAndGetAccessToken("testUser","testPassword");


        MovieRatingRequestDTO ratingRequestDTO = new MovieRatingRequestDTO(8);

        mockMvc.perform(put("/api/movies/{movieId}/rating", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingValue").value(8));

        Double average = movieRatingRepository.findAverageRatingByMovieMovie_id(1L);
        Long count = movieRatingRepository.countRatingByMovieMovie_id(1L);

        assertThat(average).isEqualTo(8.0);
        assertThat(count).isEqualTo(1L);

    }

    @Test
    @Transactional
    public void loginUserAndRateAMovieAndChangeTheRating() throws Exception {

        User user = createUser("testUser"+ UUID.randomUUID(),"testPassword","USER");

        String accessToken = loginAndGetAccessToken(user.getUsername(), "testPassword");

        MovieRatingRequestDTO ratingRequestDTO = new MovieRatingRequestDTO(8);

        mockMvc.perform(put("/api/movies/{movieId}/rating", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingValue").value(8));

        Double average = movieRatingRepository.findAverageRatingByMovieMovie_id(1L);
        Long count = movieRatingRepository.countRatingByMovieMovie_id(1L);

        assertThat(average).isEqualTo(8.0);
        assertThat(count).isEqualTo(1L);

        MovieRatingRequestDTO newRatingRequestDTO = new MovieRatingRequestDTO(5);

        mockMvc.perform(put("/api/movies/{movieId}/rating", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRatingRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingValue").value(5));


        Double newAverage = movieRatingRepository.findAverageRatingByMovieMovie_id(1L);
        Long newCount = movieRatingRepository.countRatingByMovieMovie_id(1L);

        assertThat(newAverage).isEqualTo(5.0);
        assertThat(newCount).isEqualTo(1L);


    }

    @Test
    public void averageRatingWithTwoUsers() throws Exception {
        User user = createUser("testUser"+ UUID.randomUUID(),"testPassword","USER");
        User user2 = createUser("testUser2","testPassword2","USER");

        String accessToken = loginAndGetAccessToken(user.getUsername(), "testPassword");
        String accessToken2 = loginAndGetAccessToken("testUser2", "testPassword2");

        MovieRatingRequestDTO ratingRequestDTO = new MovieRatingRequestDTO(8);
        MovieRatingRequestDTO ratingRequestDTO2 = new MovieRatingRequestDTO(4);


        mockMvc.perform(put("/api/movies/{movieId}/rating", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk());


        mockMvc.perform(put("/api/movies/{movieId}/rating", 2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO2)))
                .andDo(print())
                .andExpect(status().isOk());

        Double newAverage = movieRatingRepository.findAverageRatingByMovieMovie_id(2L);
        Long newCount = movieRatingRepository.countRatingByMovieMovie_id(2L);

        assertThat(newAverage).isEqualTo(6.0);
        assertThat(newCount).isEqualTo(2L);

    }



}
