package pl.lodz.p.it.auctionsystem.moa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Bid;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface BidRepository extends JpaRepository<Bid, Long> {
}