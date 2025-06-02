package shin_nc.psb_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shin_nc.psb_test.dto.UserResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    public UserResponse getCurrentUser(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .build();
    }
}
