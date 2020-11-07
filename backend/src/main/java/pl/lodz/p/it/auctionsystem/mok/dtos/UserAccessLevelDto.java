package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * DTO reprezentujące poziom dostępu użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAccessLevelDto {
    
    private Long id;
    
    private Long userId;
    
    private Long accessLevelId;
    
    @Enumerated(EnumType.STRING)
    private AccessLevelEnum accessLevelName;
}