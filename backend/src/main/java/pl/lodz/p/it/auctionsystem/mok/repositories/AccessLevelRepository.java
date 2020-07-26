package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;

public interface AccessLevelRepository extends JpaRepository<AccessLevel, Long> {

    @Transactional
    AccessLevel findAccessLevelByName(String name);
}
