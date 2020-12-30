package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO zawierające odpowiedź API.
 */
@AllArgsConstructor
@Getter
@Setter
public class ApiResponseDto {

    private Boolean success;

    private String message;
}