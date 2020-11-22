package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO zawierające login i hasło podane przez użytkownika podczas logowania.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{validation.username}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String username;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    @Size(max = 64, message = "{validation.max64chars}")
    private String password;
}