package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.moa.repositories.AuctionRepository;

@Service
@Transactional(rollbackFor = ApplicationException.class)
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public void addAuction(Auction auction) {

    }

    @Override
    public Page<Auction> getAuctions(Pageable pageable) {
        return auctionRepository.findAll(pageable);
    }
}
