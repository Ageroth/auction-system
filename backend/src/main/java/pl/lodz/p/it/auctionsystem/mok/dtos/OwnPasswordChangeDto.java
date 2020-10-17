package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO zawierające nowe oraz stare hasło potrzebne do weryfikacji.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnPasswordChangeDto {
    
    private String newPassword;
    
    private String oldPassword;
}
