package shin_nc.psb_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiodataResponse {

    private Long id;

    private String name;

    private String gender;

    private String religion;

    private String placeOfBirth;

    private String birthDate;

    private String address;

    private String phoneNumber;

    private String nisn;

    private String schoolFrom;
    
}
