package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * DTO zawierające informacje o poziomie dostępu.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessLevelDto {
    
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private AccessLevelEnum name;
}