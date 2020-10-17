package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO zawierające token uwierzytelniający użytkownika, nazwę użytkownika oraz jego poziomy dostępu.
 */
@AllArgsConstructor
@Getter
@Setter
public class JwtTokenDto {
    
    private String token;
    
    private String username;
    
    private List<String> roles;
}