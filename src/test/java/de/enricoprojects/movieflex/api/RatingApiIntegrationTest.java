package de.enricoprojects.movieflex.api;

import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.MovieRatingRequestDTO;
import de.enricoprojects.movieflex.dto.RegisterRequestDTO;
import de.enricoprojects.movieflex.entity.MovieRating;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.MovieRatingRepository;
import de.enricoprojects.movieflex.repository.MovieRepository;
import de.enricoprojects.movieflex.repository.UserRepository;
import jakarta.servlet.Registration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class RatingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRatingRepository movieRatingRepository;



    @Test
    @Transactional
    public void loginUserAndRateAMovie() throws Exception {

        User user = new User();

        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("email");
        user.setRole("USER");
        userRepository.save(user);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andReturn();


        String loadResponseBody = mvcResult.getResponse().getContentAsString();

        JsonNode loginJson = objectMapper.readTree(loadResponseBody);
        String accessToken = loginJson.get("accessToken").asText();

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


}
