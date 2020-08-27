package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EditUserDetailsDto {
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
}
