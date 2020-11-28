package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.Auction;

@Service
public interface AuctionService {

    void addAuction(Auction auction);

    Page<Auction> getAuctions(Pageable pageable);
}