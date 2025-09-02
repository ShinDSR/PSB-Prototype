package shin_nc.psb_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shin_nc.psb_test.dto.ParentsRequest;
import shin_nc.psb_test.dto.ParentsResponse;
import shin_nc.psb_test.dto.WebResponse;
import shin_nc.psb_test.entity.User;
import shin_nc.psb_test.service.ParentsService;

@RestController
@RequestMapping("/parents")
public class ParentsController {

    @Autowired
    private ParentsService parentsService;

    @PostMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ParentsResponse> createCurrent(Authentication authentication, @RequestBody ParentsRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        ParentsResponse parentsResponse = parentsService.createCurrent(user, request);
        return WebResponse.<ParentsResponse>builder().data(parentsResponse).build();
    }

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ParentsResponse> getCurrent(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        ParentsResponse parentsResponse = parentsService.getCurrent(user);
        return WebResponse.<ParentsResponse>builder().data(parentsResponse).build();
    }

    @PutMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ParentsResponse> updateCurrent(Authentication authentication, @RequestBody ParentsRequest request){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        ParentsResponse parentsResponse = parentsService.updateCurrent(user, request);
        return WebResponse.<ParentsResponse>builder().data(parentsResponse).build();
    }
}
