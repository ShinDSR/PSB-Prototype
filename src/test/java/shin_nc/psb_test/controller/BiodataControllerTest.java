package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import shin_nc.psb_test.dto.BiodataResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.AppGender;
import shin_nc.psb_test.entity.AppReligion;
import shin_nc.psb_test.entity.AppRole;
import shin_nc.psb_test.entity.Biodata;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;
import shin_nc.psb_test.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BiodataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BiodataRepository biodataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        biodataRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test01@psb.local");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(AppRole.USER);
        user.setToken("valid_token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day expiration
        userRepository.save(user);
    }

    //========================= Get Current Biodata Tests =========================
    @Test
    void testGetCurrentBiodataTokenNotSend() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2000-01-01");
        
        Biodata biodata = new Biodata();
        biodata.setName("Test User");
        biodata.setGender(AppGender.L);
        biodata.setReligion(AppReligion.ISLAM);
        biodata.setPlaceOfBirth("Test City");
        biodata.setBirthDate(birthDate);
        biodata.setAddress("Test Address");
        biodata.setPhoneNumber("1234567890");
        biodata.setNisn("123456789");
        biodata.setSchoolFrom("Test School");
        biodata.setUser(user);
        biodataRepository.save(biodata);

        mockMvc.perform(
            get("/biodata/current")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<BiodataResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCurrentBiodataInvalidToken() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2000-01-01");
        
        Biodata biodata = new Biodata();
        biodata.setName("Test User");
        biodata.setGender(AppGender.L);
        biodata.setReligion(AppReligion.ISLAM);
        biodata.setPlaceOfBirth("Test City");
        biodata.setBirthDate(birthDate);
        biodata.setAddress("Test Address");
        biodata.setPhoneNumber("1234567890");
        biodata.setNisn("123456789");
        biodata.setSchoolFrom("Test School");
        biodata.setUser(user);
        biodataRepository.save(biodata);

        mockMvc.perform(
            get("/biodata/current")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer invalid_token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<BiodataResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetCurrentBiodataSuccess() throws Exception {
        User user = userRepository.findByEmail("test01@psb.local").orElseThrow();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("2000-01-01");
        
        Biodata biodata = new Biodata();
        biodata.setName("Test User");
        biodata.setGender(AppGender.L);
        biodata.setReligion(AppReligion.ISLAM);
        biodata.setPlaceOfBirth("Test City");
        biodata.setBirthDate(birthDate);
        biodata.setAddress("Test Address");
        biodata.setPhoneNumber("1234567890");
        biodata.setNisn("123456789");
        biodata.setSchoolFrom("Test School");
        biodata.setUser(user);
        biodataRepository.save(biodata);

        mockMvc.perform(
            get("/biodata/current")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer valid_token")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<BiodataResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("Test User", response.getData().getName());
            assertEquals("L", response.getData().getGender());
            assertEquals("ISLAM", response.getData().getReligion());
            assertEquals("Test City", response.getData().getPlaceOfBirth());
            assertEquals("2000-01-01", response.getData().getBirthDate());
            assertEquals("Test Address", response.getData().getAddress());
            assertEquals("1234567890", response.getData().getPhoneNumber());
            assertEquals("123456789", response.getData().getNisn());
            assertEquals("Test School", response.getData().getSchoolFrom());
        });
    }

}
