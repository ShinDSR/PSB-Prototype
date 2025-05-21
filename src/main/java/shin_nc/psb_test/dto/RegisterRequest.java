package shin_nc.psb_test.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Religion is required")
    private String religion;

    @NotBlank(message = "Place of Birth is required")
    private String placeOfBirth;

    @NotBlank(message = "Date of Birth is required")
    private String birthDate;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "NISN is required")
    private String nisn;

    @NotBlank(message = "School From is required")
    private String schoolFrom;
    
}
