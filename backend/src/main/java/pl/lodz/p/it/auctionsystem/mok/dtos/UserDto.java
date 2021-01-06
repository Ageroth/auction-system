package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO zawierające wszelkie dane encji użytkownika wraz z jej wersją.
 */
@Getter
@Setter
public class UserDto {

    private Long version;

    private Long id;

    private String username;

    private String password;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private String activationCode;

    private String passwordResetCode;

    private LocalDateTime passwordResetCodeAddDate;

    private List<String> userAccessLevelNames;

    private List<Long> accessLevelIds;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}