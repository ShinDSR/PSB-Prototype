package shin_nc.psb_test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String email;
    private String password;
}
