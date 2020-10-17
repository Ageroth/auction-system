package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO ze szczegółowymi danymi użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSummaryDto {
    
    private String username;
    
    private String firstName;
    
    private String lastName;
    
    private String email;
}