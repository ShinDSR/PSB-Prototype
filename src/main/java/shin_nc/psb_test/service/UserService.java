package shin_nc.psb_test.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shin_nc.psb_test.dto.UserResponse;
import shin_nc.psb_test.dto.UserUpdateRequest;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse getCurrentUser(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponse updateCurrentUser(User user, UserUpdateRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        return UserResponse.builder()
                .email(user.getEmail())
                .build();
    }
}
