package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO wykorzystywane przy rejestracji użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupDto {
    
    @NotBlank
    private String username;
    
    @NotBlank
    @Size(min = 8)
    private String password;
    
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @NotBlank
    private String phoneNumber;
}