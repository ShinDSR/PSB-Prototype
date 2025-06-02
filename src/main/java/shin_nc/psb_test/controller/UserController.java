package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shin_nc.psb_test.dto.UserResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails; // atau casting sesuai implementasi kamu
        UserResponse userResponse = userService.getCurrentUser(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
