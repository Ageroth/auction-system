package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidDto {

    private LocalDateTime date;

    private BigDecimal price;

    private String userUsername;
}