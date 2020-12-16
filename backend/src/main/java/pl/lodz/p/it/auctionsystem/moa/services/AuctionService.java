package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionAddDto;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionCriteria;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDetailsDto;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDto;

@Service
public interface AuctionService {

    Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException;

    Page<AuctionDto> getAuctions(AuctionCriteria auctionCriteria);

    AuctionDetailsDto getAuctionById(Long auctionId) throws ApplicationException;
}