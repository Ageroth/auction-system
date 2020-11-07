package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO z informacją o dostępności.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserIdentityAvailabilityDto {

    private Boolean available;
}