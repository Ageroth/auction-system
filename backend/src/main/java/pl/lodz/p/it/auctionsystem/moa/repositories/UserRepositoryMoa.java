package pl.lodz.p.it.auctionsystem.moa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;

import java.util.Optional;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link User}.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepositoryMoa extends JpaRepository<User, Long> {

    /**
     * Pobiera z bazy danych encję {@link User} o podanej nazwie użytkownika ignorując wielkość znaków.
     *
     * @param username nazwa użytkownika
     * @return obiekt encji {@link User} opakowany w {@link Optional}
     */
    Optional<User> findByUsernameIgnoreCase(String username);
}