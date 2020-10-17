package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO wykorzystywane przy resetowaniu hasła, zawierające nowe hasło.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetDto {
    
    private String newPassword;
}