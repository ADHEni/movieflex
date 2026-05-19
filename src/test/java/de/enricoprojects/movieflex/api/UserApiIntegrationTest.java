package de.enricoprojects.movieflex.api;

import de.enricoprojects.movieflex.dto.LoginRequestDTO;
import de.enricoprojects.movieflex.dto.RegisterRequestDTO;
import de.enricoprojects.movieflex.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void registerUserTest() throws Exception {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("testUser", "testPassword", "testEmail");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("testEmail"));



    }

    @Test
    public void loginTest() throws Exception {
        User user =  createRandomUser("ROLE");

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUsername(), "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));



    }

    @Test
    public void registerLoginAndAuthenticateMeTest() throws Exception {

        String username = "testUser_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "testPassword";
        String email = username + "@test.com";

        RegisterRequestDTO registerRequestDTO =
                new RegisterRequestDTO(username, password, email);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                // falls dein Register 201 zurückgibt: .andExpect(status().isCreated())
                .andExpect(status().isOk());

        LoginRequestDTO loginRequestDTO =
                new LoginRequestDTO(username, password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.username").value(username))
                .andReturn();

        String loginResponseBody = loginResult.getResponse().getContentAsString();

        JsonNode loginJson = objectMapper.readTree(loginResponseBody);
        String accessToken = loginJson.get("accessToken").asText();

        mockMvc.perform(get("/api/users/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    public void loginWithWrongPasswordTest() throws Exception {
        String username = "testUser_" + UUID.randomUUID().toString().substring(0, 8);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, "wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void registerWithAUsernameThatAlreadyExistsTest() throws Exception {
        User user =  createRandomUser("ROLE");

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(user.getUsername(), "password", "email");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("USERNAME_ALREADY_EXISTS"));

    }



}
