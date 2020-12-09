package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionAddDto {

    private BigDecimal startingPrice;

    private LocalDateTime startDate;

    private int duration;

    private String itemName;

    private String itemDescription;

    private byte[] image;

    private String username;
}