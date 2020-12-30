package pl.lodz.p.it.auctionsystem.moa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link Auction}.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {
}