package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO zawierające nowe hasło, wykorzystywane przy jego resecie.
 */
@Getter
@Setter
public class PasswordResetDto {

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    @Size(max = 64, message = "{validation.max64chars}")
    private String newPassword;
}