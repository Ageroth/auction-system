package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO z nowym hasłem użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPasswordChangeDto {
    
    private String newPassword;
}
