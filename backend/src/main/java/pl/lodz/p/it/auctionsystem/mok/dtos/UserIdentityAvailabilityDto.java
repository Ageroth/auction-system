package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO zawierające informację o dostępności adresu email lub nazwy użytkownika.
 */
@AllArgsConstructor
@Getter
@Setter
public class UserIdentityAvailabilityDto {

    private boolean available;
}