package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO wykorzystywane przy wysyłaniu łącza do resetu hasła.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetEmailDto {
    
    private String email;
}