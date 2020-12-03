package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO zawierające szczegóły własnego konta.
 */
@Getter
@Setter
public class OwnAccountDetailsDto {

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}