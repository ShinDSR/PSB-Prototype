package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shin_nc.psb_test.dto.UserResponse;
import shin_nc.psb_test.dto.UserUpdateRequest;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getCurrent(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        UserResponse userResponse = userService.getCurrent(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> updateCurrent(Authentication authentication, @RequestBody UserUpdateRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        UserResponse userResponse = userService.updateCurrent(user, request);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
