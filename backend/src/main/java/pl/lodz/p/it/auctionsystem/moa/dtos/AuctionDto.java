package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO zawierające dane aukcji, wykorzystywane do wyświetlania ich listy.
 */
@Getter
@Setter
public class AuctionDto {

    private Long id;

    private BigDecimal startingPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int bidsNumber;

    private BigDecimal currentPrice;

    private String userUsername;

    private String itemName;

    private String itemDescription;

    private byte[] itemImage;

    private String topBidderName;
}