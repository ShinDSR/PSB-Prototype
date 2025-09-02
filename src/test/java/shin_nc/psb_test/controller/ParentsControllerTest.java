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

import shin_nc.psb_test.dto.ParentsRequest;
import shin_nc.psb_test.dto.ParentsResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.AppRole;
import shin_nc.psb_test.entity.Parents;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;
import shin_nc.psb_test.repository.ParentsRepository;
import shin_nc.psb_test.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ParentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private ParentsRepository parentsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        biodataRepository.deleteAll();
        userRepository.deleteAll();
        parentsRepository.deleteAll();

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);

    }

    //========================= Create Current Parents Tests =========================
    @Test
    void testCreateCurrentParentsSuccess() throws Exception {

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("Ayah Test");
        request.setFatherJob("Petani");
        request.setFatherPhoneNumber("081111111111");
        request.setMotherName("Ibu Test");
        request.setMotherJob("Guru");
        request.setMotherPhoneNumber("082222222222");

        mockMvc.perform(
                post("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getId());
                    assertEquals("Ayah Test", response.getData().getFatherName());
                    assertEquals("Petani", response.getData().getFatherJob());
                    assertEquals("081111111111", response.getData().getFatherPhoneNumber());
                    assertEquals("Ibu Test", response.getData().getMotherName());
                    assertEquals("Guru", response.getData().getMotherJob());
                    assertEquals("082222222222", response.getData().getMotherPhoneNumber());
                });
    }

    @Test
    void testCreateCurrentParentsBadRequest() throws Exception {

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("");
        request.setFatherJob("");
        request.setFatherPhoneNumber("");
        request.setMotherName("");
        request.setMotherJob("");
        request.setMotherPhoneNumber("");

        mockMvc.perform(
                post("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateCurrentParentsUnauthorized() throws Exception {

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("Ayah Test");
        request.setFatherJob("Petani");
        request.setFatherPhoneNumber("081111111111");
        request.setMotherName("Ibu Test");
        request.setMotherJob("Guru");
        request.setMotherPhoneNumber("082222222222");

        mockMvc.perform(
                post("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }


    //========================= Get Current Parents Tests =========================
    @Test
    void testGetCurrentParentsSuccess() throws Exception{
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        Parents parents = new Parents();
        parents.setFatherName("Ayah Test");
        parents.setFatherJob("Petani");
        parents.setFatherPhoneNumber("081111111111");
        parents.setMotherName("Ibu Test");
        parents.setMotherJob("Guru");
        parents.setMotherPhoneNumber("082222222222");
        parents.setUser(user);
        parentsRepository.save(parents);

        mockMvc.perform(
                get("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getId());
                    assertEquals("Ayah Test", response.getData().getFatherName());
                    assertEquals("Petani", response.getData().getFatherJob());
                    assertEquals("081111111111", response.getData().getFatherPhoneNumber());
                    assertEquals("Ibu Test", response.getData().getMotherName());
                    assertEquals("Guru", response.getData().getMotherJob());
                    assertEquals("082222222222", response.getData().getMotherPhoneNumber());
                });        
    }

    @Test
    void testGetCurrentParentsNotFound() throws Exception{
        mockMvc.perform(
                get("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });        
    }

    @Test
    void testGetCurrentParentsUnauthorized() throws Exception{
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        Parents parents = new Parents();
        parents.setFatherName("Ayah Test");
        parents.setFatherJob("Petani");
        parents.setFatherPhoneNumber("081111111111");
        parents.setMotherName("Ibu Test");
        parents.setMotherJob("Guru");
        parents.setMotherPhoneNumber("082222222222");
        parents.setUser(user);
        parentsRepository.save(parents);

        mockMvc.perform(
                get("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalid_token"))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });        
    }

    //========================= Update Current Parents Tests =========================
    @Test
    void testUpdateCurrentParentsSuccess() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        Parents parents = new Parents();
        parents.setFatherName("Ayah Test");
        parents.setFatherJob("Petani");
        parents.setFatherPhoneNumber("081111111111");
        parents.setMotherName("Ibu Test");
        parents.setMotherJob("Guru");
        parents.setMotherPhoneNumber("082222222222");
        parents.setUser(user);
        parentsRepository.save(parents);

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("Ayah Update");
        request.setFatherJob("Petani Update");
        request.setFatherPhoneNumber("081111111112");
        request.setMotherName("Ibu Update");
        request.setMotherJob("Guru Update");
        request.setMotherPhoneNumber("082222222221");

        mockMvc.perform(
                put("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getId());
                    assertEquals("Ayah Update", response.getData().getFatherName());
                    assertEquals("Petani Update", response.getData().getFatherJob());
                    assertEquals("081111111112", response.getData().getFatherPhoneNumber());
                    assertEquals("Ibu Update", response.getData().getMotherName());
                    assertEquals("Guru Update", response.getData().getMotherJob());
                    assertEquals("082222222221", response.getData().getMotherPhoneNumber());
                });
    }

    @Test
    void testUpdateCurrentParentsUnauthorized() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        Parents parents = new Parents();
        parents.setFatherName("Ayah Test");
        parents.setFatherJob("Petani");
        parents.setFatherPhoneNumber("081111111111");
        parents.setMotherName("Ibu Test");
        parents.setMotherJob("Guru");
        parents.setMotherPhoneNumber("082222222222");
        parents.setUser(user);
        parentsRepository.save(parents);

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("Ayah Update");
        request.setFatherJob("Petani Update");
        request.setFatherPhoneNumber("081111111112");
        request.setMotherName("Ibu Update");
        request.setMotherJob("Guru Update");
        request.setMotherPhoneNumber("082222222221");

        mockMvc.perform(
                put("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateCurrentParentsBadRequest() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        Parents parents = new Parents();
        parents.setFatherName("Ayah Test");
        parents.setFatherJob("Petani");
        parents.setFatherPhoneNumber("081111111111");
        parents.setMotherName("Ibu Test");
        parents.setMotherJob("Guru");
        parents.setMotherPhoneNumber("082222222222");
        parents.setUser(user);
        parentsRepository.save(parents);

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("");
        request.setFatherJob("");
        request.setFatherPhoneNumber("");
        request.setMotherName("");
        request.setMotherJob("");
        request.setMotherPhoneNumber("");

        mockMvc.perform(
                put("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateCurrentParentsNotFound() throws Exception {

        ParentsRequest request = new ParentsRequest();
        request.setFatherName("Ayah Update");
        request.setFatherJob("Petani Update");
        request.setFatherPhoneNumber("081111111112");
        request.setMotherName("Ibu Update");
        request.setMotherJob("Guru Update");
        request.setMotherPhoneNumber("082222222221");

        mockMvc.perform(
                put("/parents/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid_token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<ParentsResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }
}
