package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO wykorzystywane przy aktualizacji danych personalnych użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsUpdateDto {

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
    private List<Long> accessLevelIds = new ArrayList<>();
}