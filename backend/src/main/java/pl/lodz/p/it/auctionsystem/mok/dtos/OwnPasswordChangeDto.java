package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO zawierające nowe oraz stare hasło potrzebne do weryfikacji.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnPasswordChangeDto {

    private String username;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    @Size(max = 64, message = "{validation.max64chars}")
    private String newPassword;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    @Size(max = 64, message = "{validation.max64chars}")
    private String currentPassword;
}