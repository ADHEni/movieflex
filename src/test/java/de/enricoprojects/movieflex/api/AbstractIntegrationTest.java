package de.enricoprojects.movieflex.api;


import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    protected User createUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(username + "@test.com");
        user.setRole(role);

        return userRepository.save(user);
    }

    protected User createRandomUser(String role) {
        String username = "user_" + UUID.randomUUID().toString().substring(0, 8);
        return createUser(username, "password", role);
    }

    protected User createRandomAdmin() {
        String username = "admin_" + UUID.randomUUID().toString().substring(0, 8);
        return createUser(username, "admin", "ADMIN");
    }

    protected String loginAndGetAccessToken(String username, String password) throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode loginJson = objectMapper.readTree(responseBody);

        return loginJson.get("accessToken").asText();
    }


}
