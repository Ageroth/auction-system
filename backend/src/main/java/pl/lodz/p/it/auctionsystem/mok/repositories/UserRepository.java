package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByLogin(String login);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByLogin(String login);
    
    Optional<User> findByActivationCode(String activationCode);
    
    Optional<User> findByResetPasswordCode(String resetPasswordCode);
}
