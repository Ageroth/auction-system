package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * DTO zawierające login i hasło podane przez użytkownika podczas logowania.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
}