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

import shin_nc.psb_test.dto.LoginRequest;
import shin_nc.psb_test.dto.LoginResponse;
import shin_nc.psb_test.dto.RegisterRequest;
import shin_nc.psb_test.dto.RegisterResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.AppGender;
import shin_nc.psb_test.entity.AppReligion;
import shin_nc.psb_test.entity.AppRole;
import shin_nc.psb_test.entity.Biodata;
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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        biodataRepository.deleteAll();
        userRepository.deleteAll();
        registrationRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setGender("L");
        request.setReligion("ISLAM");
        request.setPlaceOfBirth("Jakarta");
        request.setBirthDate("2000-01-01");
        request.setAddress("Yogyakarta");
        request.setPhoneNumber("08123456789");
        request.setNisn("1234567890");
        request.setSchoolFrom("SMA Negeri 1");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan tidak ada error
            assertNull(response.getErrors());

            // Pastikan email dan password dihasilkan
            RegisterResponse registerResponse = response.getData();
            assertNotNull(registerResponse);
            assertNotNull(registerResponse.getEmail());
            assertTrue(registerResponse.getEmail().contains("@psb.local"));

            assertNotNull(registerResponse.getPassword());
            assertTrue(registerResponse.getPassword().length() >= 8);

            // Optional: Cetak hasil email & password (debug)
            System.out.println("Generated Email: " + registerResponse.getEmail());
            System.out.println("Generated Password: " + registerResponse.getPassword());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("");
        request.setGender("");
        request.setReligion("");
        request.setPlaceOfBirth("");
        request.setBirthDate("");
        request.setAddress("");
        request.setPhoneNumber("");
        request.setNisn("");
        request.setSchoolFrom("");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan ada error
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicateNisn() throws Exception{
        Biodata biodata = new Biodata();
        biodata.setName("Jane Doe");
        biodata.setGender(AppGender.L);
        biodata.setReligion(AppReligion.ISLAM);
        biodata.setPlaceOfBirth("Jakarta");
        biodata.setBirthDate(java.sql.Date.valueOf("2000-01-01"));
        biodata.setAddress("Yogyakarta");
        biodata.setPhoneNumber("08123456789");
        biodata.setNisn("1234567890");
        biodata.setSchoolFrom("SMA Negeri 1");
        biodataRepository.save(biodata);

        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setGender("L");
        request.setReligion("ISLAM");
        request.setPlaceOfBirth("Jakarta");
        request.setBirthDate("2000-01-01");
        request.setAddress("Yogyakarta");
        request.setPhoneNumber("08123456789");
        request.setNisn("1234567890");
        request.setSchoolFrom("SMA Negeri 1");
        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan ada error
            assertNotNull(response.getErrors());
        });
    }


    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setEmail("test001@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("test001@psb.local");
        request.setPassword("password");
        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan tidak ada error
            assertNull(response.getErrors());

            // Pastikan token tidak null
            assertNotNull(response.getData().getToken());
        });
    }

    @Test
    void loginEmailNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test001@psb.local");
        request.setPassword("password");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan ada error
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginWrongPassword() throws Exception {
        User user = new User();
        user.setEmail("test001@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        userRepository.save(user);
        
        LoginRequest request = new LoginRequest();
        request.setEmail("test001@psb.local");
        request.setPassword("wrongpassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan ada error
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void loginBadRequest() throws Exception {
        User user = new User();
        user.setEmail("test001@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            // Pastikan ada error
            assertNotNull(response.getErrors());
        });
    }

}
