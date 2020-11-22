package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO wykorzystywane przy wysyłaniu łącza do resetu hasła.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetEmailDto {

    @NotBlank(message = "{validation.notBlank}")
    @Email(message = "{validation.email}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String email;
}