package shin_nc.psb_test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import shin_nc.psb_test.dto.UserResponse;
import shin_nc.psb_test.dto.UserUpdateRequest;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.AppRole;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;
import shin_nc.psb_test.repository.RegistrationRepository;
import shin_nc.psb_test.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetCurrentUserSucces() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("test01@psb.local", response.getData().getEmail());
        });
    }

    @Test
    void testGetCurrentUserInvalidToken() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer invalid_token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCurrentUserTokenNotSend() throws Exception {

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCurrentUserTokenExpired() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        mockMvc.perform(
                get("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserSucces() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("new_password");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("test01@psb.local", response.getData().getEmail());

            User userDB = userRepository.findByEmail("test01@psb.local").orElseThrow();
            assertNotNull(userDB);
            assertTrue(passwordEncoder.matches("new_password", userDB.getPassword()));
        });
    }

    @Test
    void testUpdateCurrentUserBadRequest() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("short");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserInvalidToken() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("new_password");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer invalid_token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserTokenNotSend() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("new_password");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateCurrentUserTokenExpired() throws Exception {

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("new_password");

        mockMvc.perform(
                patch("/users/current")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

}
