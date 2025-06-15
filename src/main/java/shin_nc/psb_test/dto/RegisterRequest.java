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
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Religion is required")
    private String religion;

    @NotBlank(message = "Place of Birth is required")
    @Size(max = 100, message = "Place of Birth must be less than 100 characters")
    private String placeOfBirth;

    @NotBlank(message = "Date of Birth is required")
    private String birthDate;

    @NotBlank(message = "Address is required")
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @NotBlank(message = "Phone Number is required")
    @Size(max = 15, min = 10, message = "Phone Number must be between 10 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "NISN is required")
    @Size(max = 10, min = 10, message = "NISN must be exactly 10 characters")
    private String nisn;

    @NotBlank(message = "School From is required")
    @Size(max = 100, message = "School From must be less than 100 characters")
    private String schoolFrom;
    
}
