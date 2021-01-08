package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO zawierające szczegóły oferty, wykorzystywane przy jej stawianiu.
 */
@Getter
@Setter
public class BidPlaceDto {

    @NotNull(message = "{validation.notBlank}")
    @DecimalMin(value = "0.01", message = "{validation.decimalMin}")
    private BigDecimal price;
}