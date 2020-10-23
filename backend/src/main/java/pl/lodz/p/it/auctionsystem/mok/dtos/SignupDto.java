package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * DTO wykorzystywane przy rejestracji użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupDto {
    
    private String username;
    
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    private String password;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
}