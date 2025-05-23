package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shin_nc.psb_test.dto.LoginRequest;
import shin_nc.psb_test.dto.LoginResponse;
import shin_nc.psb_test.dto.RegisterRequest;
import shin_nc.psb_test.dto.RegisterResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.register(request);
        return WebResponse.<RegisterResponse>builder().data(registerResponse).build();
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return WebResponse.<LoginResponse>builder().data(loginResponse).build();
    }
}
