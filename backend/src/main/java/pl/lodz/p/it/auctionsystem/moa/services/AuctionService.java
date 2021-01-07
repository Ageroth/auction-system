package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.moa.dtos.*;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link Auction}
 */
@Service
public interface AuctionService {

    /**
     * Tworzy aukcję.
     *
     * @param auctionAddDto obiekt typu {@link AuctionAddDto}
     * @return id nowo utworzonego obiektu
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException;

    /**
     * Zwraca aukcje spełniające zadane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @return obiekt typu {@link Page}
     */
    Page<BasicAuctionDto> getAuctions(AuctionCriteria auctionCriteria);

    /**
     * Zwraca aukcje danego użykownika, spełniające zadane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @param username        nazwa użytkownika
     * @return obiekt typu {@link Page}
     */
    Page<BasicAuctionDto> getAuctionsByUsername(AuctionCriteria auctionCriteria, String username);

    /**
     * Zwraca aukcje, w których dany użytkownik bierze udział, spełniające zadane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @param username        nazwa użytkownika
     * @return obiekt typu {@link Page}
     */
    Page<BasicAuctionDto> getParticipatedAuctions(AuctionCriteria auctionCriteria, String username);

    /**
     * Zwraca aukcję o podanym id.
     *
     * @param auctionId id aukcji
     * @return obiekt typu {@link AuctionDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    AuctionDto getAuctionById(Long auctionId) throws ApplicationException;

    /**
     * Zwraca własną aukcję o podanym id.
     *
     * @param auctionId id aukcji
     * @return obiekt typu {@link AuctionDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    AuctionDto getOwnAuctionById(Long auctionId) throws ApplicationException;

    /**
     * Zwraca aukcję o podanym id, w której użytkownik bierze udział.
     *
     * @param auctionId id aukcji
     * @param username  nazwa użytkownika
     * @return obiekt typu {@link AuctionDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    AuctionDto getOwnBiddingById(Long auctionId, String username) throws ApplicationException;

    /**
     * Aktualizuje dane aukcji o podanym id.
     *
     * @param auctionId        id aukcji
     * @param auctionUpdateDto obiekt typu {@link AuctionUpdateDto}
     * @param username         nazwa aktualnie zalogowanego użytkownika
     * @param ifMatch          wartość nagłówka If-Match będącego wartością pola wersji obiektu
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void updateAuctionById(Long auctionId, AuctionUpdateDto auctionUpdateDto, String username, String ifMatch) throws ApplicationException;

    /**
     * Dodaje nową ofertę do aukcji o podanym id.
     *
     * @param auctionId   id aukcji
     * @param bidPlaceDto obiekt typu {@link BidPlaceDto}
     * @return id nowo utworzonego obiektu
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    Long addBid(Long auctionId, BidPlaceDto bidPlaceDto) throws ApplicationException;

    /**
     * Usuwa aukcję o podanym id.
     *
     * @param auctionId id aukcji
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void deleteAuctionById(Long auctionId, String username) throws ApplicationException;
}