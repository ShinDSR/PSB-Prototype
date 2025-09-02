package shin_nc.psb_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import shin_nc.psb_test.dto.LoginRequest;
import shin_nc.psb_test.dto.LoginResponse;
import shin_nc.psb_test.dto.RegisterRequest;
import shin_nc.psb_test.dto.RegisterResponse;
import shin_nc.psb_test.entity.AppGender;
import shin_nc.psb_test.entity.AppReligion;
import shin_nc.psb_test.entity.AppRole;
import shin_nc.psb_test.entity.Biodata;
import shin_nc.psb_test.entity.Registration;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.BiodataRepository;
import shin_nc.psb_test.repository.UserRepository;
// import shin_nc.psb_test.security.JwtService;

@Service
public class AuthService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BiodataRepository biodataRepository;

    // @Autowired
    // private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        validationService.validate(request);

        //Validasi: jika NISN sudah digunakan, tolak pendaftaran
        if (biodataRepository.findByNisn(request.getNisn()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NISN already used");
        }

        // Generate email
        String baseEmailName = request.getName().trim().toLowerCase().replaceAll("\\s+", ".");
        String randomDigits = String.format("%03d", (int) (Math.random() * 1000));
        String generatedEmail = baseEmailName + randomDigits + "@psb.local";

        // Generate password
        String rawPassword = generateRandomPassword(8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Set User
        User user = new User();
        user.setEmail(generatedEmail);
        user.setPassword(hashedPassword);
        user.setRole(AppRole.USER);

        // Set Biodata
        Biodata biodata = new Biodata();
        biodata.setName(request.getName());
        biodata.setGender(AppGender.valueOf(request.getGender().toUpperCase()));
        biodata.setReligion(AppReligion.valueOf(request.getReligion().toUpperCase()));
        biodata.setPlaceOfBirth(request.getPlaceOfBirth());
        biodata.setBirthDate(java.sql.Date.valueOf(request.getBirthDate()));
        biodata.setAddress(request.getAddress());
        biodata.setPhoneNumber(request.getPhoneNumber());
        biodata.setNisn(request.getNisn());
        biodata.setSchoolFrom(request.getSchoolFrom());

        biodata.setUser(user);
        user.setBiodata(biodata);

        // Set Registration
        Registration registration = new Registration();
        registration.setRegistrationDate(java.time.LocalDate.now());
        registration.setStatus("pending");
        registration.setUser(user);
        user.setRegistration(registration);

        // Save all
        userRepository.save(user);

        // Return response
        return RegisterResponse.builder()
                .email(user.getEmail())
                .password(rawPassword)
                .build();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Transactional
    public LoginResponse login(LoginRequest request){

        validationService.validate(request);

        // Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        // Check if password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Generate JWT token
        // String jwtToken = jwtService.generateToken(user.getEmail());
        // Long tokenExpiredAt = jwtService.tokenExpiredAt();

        // Generate random token
        String token = generateRandomToken(150);
        
        // Update user token and token expiration
        user.setToken(token);
        user.setTokenExpiredAt(next1Day());
        userRepository.save(user);

        return LoginResponse.builder()
                .token(user.getToken())
                .tokenExpiredAt(user.getTokenExpiredAt())
                .build();
    }

    private String generateRandomToken(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private Long next1Day() {
        return System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 1 day
    }


    @Transactional
    public void logout(User user) {

        user.setToken(null);
        user.setTokenExpiredAt(null);
        
        userRepository.save(user);
    }
}
