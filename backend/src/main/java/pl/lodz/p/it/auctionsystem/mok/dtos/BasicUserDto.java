package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO zawierające dane kont użytkowników, wykorzystywane do wyświetlania ich listy.
 */
@Getter
@Setter
public class BasicUserDto {

    private Long id;

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private List<String> userAccessLevelNames;

    private String firstName;

    private String lastName;
}