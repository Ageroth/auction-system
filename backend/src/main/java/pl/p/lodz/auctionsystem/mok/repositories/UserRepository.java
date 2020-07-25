package pl.p.lodz.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.p.lodz.auctionsystem.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    boolean existsUserByLogin(String login);

    @Transactional
    boolean existsUserByEmail(String email);

    @Transactional
    Optional<User> findUserByLogin(String login);
}
