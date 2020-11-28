package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuctionDto {

    private Long id;

    private String userUsername;

    private String itemName;

    private String itemDescription;

    private BigDecimal openingPrice;

    private int bidsNumber;

    private BigDecimal currentPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}