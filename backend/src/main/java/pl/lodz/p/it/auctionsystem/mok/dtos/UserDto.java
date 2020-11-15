package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DTO z danymi wykorzystywane do wyświetlania listy użytkowników.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;
}