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
public class ParentsRequest {

    @NotBlank(message = "Father's name is required")
    @Size(max = 100, message = "Father's name must be at most 100 characters")
    private String fatherName;

    @NotBlank(message = "Father's job is required")
    @Size(max = 100, message = "Father's job must be at most 100 characters")
    private String fatherJob;

    @NotBlank(message = "Father's phone number is required")
    @Size(min = 11, max = 15, message = "Father's phone number must be between 11 and 15 characters")
    private String fatherPhoneNumber;

    @NotBlank(message = "Mother's name is required")
    @Size(max = 100, message = "Mother's name must be at most 100 characters")
    private String motherName;

    @NotBlank(message = "Mother's job is required")
    @Size(max = 100, message = "Mother's job must be at most 100 characters")
    private String motherJob;

    @NotBlank(message = "Mother's phone number is required")
    @Size(min = 11, max = 15, message = "Mother's phone number must be between 11 and 15 characters")
    private String motherPhoneNumber;
}
