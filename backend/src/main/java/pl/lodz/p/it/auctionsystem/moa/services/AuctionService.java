package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.moa.dtos.*;

@Service
public interface AuctionService {

    Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException;

    Page<AuctionDto> getAuctions(AuctionCriteria auctionCriteria);

    AuctionDetailsDto getAuctionById(Long auctionId) throws ApplicationException;

    /**
     * Aktualizuje dane aukcji o podanym id.
     *
     * @param auctionId      id aukcji
     * @param auctionEditDto obiekt typu {@link AuctionEditDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateAuctionById(Long auctionId, AuctionEditDto auctionEditDto) throws ApplicationException;
}