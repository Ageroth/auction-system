package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;

import java.util.Optional;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link User}.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    /**
     * Sprawdza czy istnieje encja {@link User} o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return true jeśli istnieje, w przeciwnym wypadku false
     */
    boolean existsByUsername(String username);
    
    /**
     * Sprawdza czy istnieje encja {@link User} o podanym emailu.
     *
     * @param email email użytkownika
     * @return true jeśli istnieje, w przeciwnym wypadku false
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Wyciąga z bazy danych encję {@link User} o podanej nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return obiekt encji {@link User} opakowany w {@link Optional}
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Wyciąga z bazy danych encję {@link User} o podanym emailu.
     *
     * @param email email użytkownika
     * @return obiekt encji {@link User} opakowany w {@link Optional}
     */
    Optional<User> findByEmailIgnoreCase(String email);
    
    /**
     * Wyciąga z bazy danych encję {@link User} o podanym kodzie aktywacyjnym.
     *
     * @param activationCode kod aktywacyjny użytkownika
     * @return obiekt encji {@link User} opakowany w {@link Optional}
     */
    Optional<User> findByActivationCode(String activationCode);
    
    /**
     * Wyciąga z bazy danych encję {@link User} o podanym kodzie resetującym hasło.
     *
     * @param passwordResetCode kod resetujący hasło użytkownika
     * @return obiekt encji {@link User} opakowany w {@link Optional}
     */
    Optional<User> findByPasswordResetCode(String passwordResetCode);
}