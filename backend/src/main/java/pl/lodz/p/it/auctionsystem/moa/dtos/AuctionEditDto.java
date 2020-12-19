package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AuctionEditDto {

    private BigDecimal startingPrice;

    private String itemName;

    private String itemDescription;
}