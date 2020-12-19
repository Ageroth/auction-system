package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionDetailsDto {

    private Long id;

    private BigDecimal startingPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String userUsername;

    private String itemName;

    private String itemDescription;

    private byte[] itemImage;

    private List<BidDto> bids;
}