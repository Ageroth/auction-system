package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO zawierające szczegóły aukcji, wykorzystywane do jej aktualizacji.
 */
@Getter
@Setter
public class AuctionUpdateDto {

    private BigDecimal startingPrice;

    private String itemName;

    private String itemDescription;
}