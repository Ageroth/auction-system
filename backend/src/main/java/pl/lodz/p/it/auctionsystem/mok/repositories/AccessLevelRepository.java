package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

@Repository
@Transactional
public interface AccessLevelRepository extends JpaRepository<AccessLevel, Long> {
    
    AccessLevel findByName(AccessLevelEnum name);
}