package pl.lodz.p.it.auctionsystem.moa.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionCriteria {

    private int page = 0;

    private String sortField;

    private String order;

    private String query;

    private Boolean status;
}
