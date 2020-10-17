package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO wykorzystywane przy aktualizacji danych personalnych użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsUpdateDto {
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
}
