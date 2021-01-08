package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO zawierające szczegóły konta użytkownika, wykorzystywane do ich wyświetlenia.
 */
@Getter
@Setter
public class UserAccountDetailsDto {

    private String username;

    private String email;

    private boolean activated;

    private LocalDateTime createdAt;

    private List<Long> accessLevelIds;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    public UserAccountDetailsDto(UserDto userDto) {
        this.username = userDto.getUsername();
        this.email = userDto.getEmail();
        this.activated = userDto.isActivated();
        this.createdAt = userDto.getCreatedAt();
        this.accessLevelIds = userDto.getAccessLevelIds();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.phoneNumber = userDto.getPhoneNumber();
    }
}
