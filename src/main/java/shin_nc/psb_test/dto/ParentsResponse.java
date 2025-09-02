package shin_nc.psb_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentsResponse {

    private Long id;

    private String fatherName;

    private String fatherJob;

    private String fatherPhoneNumber;

    private String motherName;

    private String motherJob;
    
    private String motherPhoneNumber;

    // private USerResponse user;
}
