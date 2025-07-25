package de.telran.gardenStore.dto.security;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Should be a well-formed email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^\\w{5,}$", message = "Password should have at least 5 characters")
    private String password;
}
