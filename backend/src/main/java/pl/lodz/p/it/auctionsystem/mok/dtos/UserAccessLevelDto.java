package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAccessLevelDto {
    
    private Long id;
    
    private Long userId;
    
    private Long accessLevelId;
    
    @Enumerated(EnumType.STRING)
    private AccessLevelEnum accessLevelName;
}
