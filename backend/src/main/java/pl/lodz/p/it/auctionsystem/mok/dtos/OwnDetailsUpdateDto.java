package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO wykorzystywane przy aktualizacji własnych danych personalnych.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnDetailsUpdateDto {

    private String username;

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