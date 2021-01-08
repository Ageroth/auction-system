package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.utils.AccessLevelEnum;

import java.util.Optional;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link AccessLevel}.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AccessLevelRepository extends JpaRepository<AccessLevel, Long> {

    /**
     * Wyciąga z bazy danych encję {@link AccessLevel} o podanej nazwie.
     *
     * @param name nazwa poziomu dostępu
     * @return obiekt encji {@link AccessLevel} opakowany w {@link Optional}
     */
    Optional<AccessLevel> findByName(AccessLevelEnum name);
}