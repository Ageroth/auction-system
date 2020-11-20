package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO z danymi wykorzystywane do wyświetlania szczegółów użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDto {

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private List<Long> accessLevelIds;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
