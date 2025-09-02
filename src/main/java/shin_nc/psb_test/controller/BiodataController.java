package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shin_nc.psb_test.dto.BiodataResponse;
import shin_nc.psb_test.dto.BiodataUpdateRequest;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.service.BiodataService;

@RestController
@RequestMapping("/biodata")
public class BiodataController {

    @Autowired
    private BiodataService biodataService;

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<BiodataResponse> getCurrent(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        BiodataResponse biodataResponse = biodataService.getCurrent(user);
        return WebResponse.<BiodataResponse>builder().data(biodataResponse).build();
    }

    @PutMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<BiodataResponse> updateCurrent(Authentication authentication, @RequestBody BiodataUpdateRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        BiodataResponse updatedBiodata = biodataService.updateCurrent(user, request);
        return WebResponse.<BiodataResponse>builder().data(updatedBiodata).build();
    }
}
