package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO zawierające szczegóły aukcji, wykorzystywane do jej aktualizacji.
 */
@Getter
@Setter
public class AuctionUpdateDto {

    @NotNull(message = "{validation.notBlank}")
    @DecimalMin(value = "0.01", message = "{validation.decimalMin}")
    private BigDecimal startingPrice;

    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 32, message = "{validation.max32chars}")
    private String itemName;

    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 4096, message = "{validation.max4096chars}")
    private String itemDescription;
}