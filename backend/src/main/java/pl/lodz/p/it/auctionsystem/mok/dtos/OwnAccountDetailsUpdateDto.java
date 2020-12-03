package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO zawierające szczegóły własnego konta, wykorzystywane do jego aktualizacji.
 */
@Getter
@Setter
public class OwnAccountDetailsUpdateDto {

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
}