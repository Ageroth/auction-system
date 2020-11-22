package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

/**
 * DTO wykorzystywane przy dodawaniu użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAddDto {

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{validation.username}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String username;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}:\";'<>?,./+=])(?=\\S+$).{8,}$", message =
            "{validation.password}")
    @Size(max = 64, message = "{validation.max64chars}")
    private String password;

    @NotBlank(message = "{validation.notBlank}")
    @Email(message = "{validation.email}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String email;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$", message = "{validation.name}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String firstName;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$", message = "{validation.name}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String lastName;

    @NotBlank(message = "{validation.notBlank}")
    @Pattern(regexp = "^[0-9]{9,10}$", message = "{validation.phoneNumber}")
    private String phoneNumber;

    @NotNull(message = "{validation.notBlank}")
    private List<Long> accessLevelIds;
}