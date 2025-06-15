package shin_nc.psb_test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiodataUpdateRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank
    private String gender;

    @NotBlank
    private String religion;

    @NotBlank(message = "Place of birth cannot be blank")
    @Size(max = 100, message = "Place of birth must be less than 100 characters")
    private String placeOfBirth;

    @NotBlank(message = "Birth date cannot be blank")
    private String birthDate;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 15, min = 10, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "NISN cannot be blank")
    @Size(max = 10, min = 10, message = "NISN must be exactly 10 characters")
    private String nisn;

    @NotBlank(message = "School from cannot be blank")
    @Size(max = 100, message = "School from must be less than 100 characters")
    private String schoolFrom;
    
}
