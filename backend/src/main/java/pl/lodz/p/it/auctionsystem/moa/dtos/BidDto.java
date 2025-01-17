package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO zawierające szczegóły oferty.
 */
@Getter
@Setter
public class BidDto {

    private Long id;

    private LocalDateTime date;

    private BigDecimal price;

    private String userUsername;
}