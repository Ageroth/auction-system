package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO z danymi wykorzystywane do wyświetlania listy użytkowników.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private boolean activated;
    
    private String firstName;
    
    private String lastName;
}