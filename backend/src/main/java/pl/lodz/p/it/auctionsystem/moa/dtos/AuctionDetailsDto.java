package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO zawierające szczegóły aukcji, wykorzystywane do ich wyświetlenia.
 */
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

    public AuctionDetailsDto(AuctionDto auctionDto) {
        this.id = auctionDto.getId();
        this.startingPrice = auctionDto.getStartingPrice();
        this.startDate = auctionDto.getStartDate();
        this.endDate = auctionDto.getEndDate();
        this.userUsername = auctionDto.getUserUsername();
        this.itemName = auctionDto.getItemName();
        this.itemDescription = auctionDto.getItemDescription();
        this.itemImage = auctionDto.getItemImage();
        this.bids = auctionDto.getBids();
    }
}