package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO ze szczegółowymi danymi użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSummaryDto {

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}