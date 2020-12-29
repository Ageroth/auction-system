package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO zawierające szczegóły oferty, wykorzystywane przy jej stawianiu.
 */
@Getter
@Setter
public class BidPlaceDto {

    private BigDecimal price;

    private String username;
}