package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO reprezentujące odpowiedź api.
 */
@AllArgsConstructor
@Getter
@Setter
public class ApiResponseDto {
    
    private Boolean success;
    
    private String message;
}