package pl.lodz.p.it.auctionsystem.moa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link Auction}.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {

    /**
     * Pobiera z bazy danych encję {@link Auction} o podanym id oraz zakłada na nią blokadę pesymistyczną.
     *
     * @param id id aukcji
     * @return obiekt encji {@link Auction} opakowany w {@link Optional}
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Auction a WHERE a.id = ?1")
    Optional<Auction> findByIdWithLock(Long id);
}