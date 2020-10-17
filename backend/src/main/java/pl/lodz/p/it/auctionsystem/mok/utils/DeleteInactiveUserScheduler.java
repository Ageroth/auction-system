package pl.lodz.p.it.auctionsystem.mok.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.auctionsystem.mok.utils.UserSpecs.isActive;

/**
 * Klasa służaca do harmonogramowania cyklicznego usuwania nieaktywnych użytkowników.
 */
@Component
public class DeleteInactiveUserScheduler {
    
    private static final Logger log = LoggerFactory.getLogger(DeleteInactiveUserScheduler.class);
    
    private final UserRepository userRepository;
    
    @Autowired
    public DeleteInactiveUserScheduler(UserRepository userRepository) {this.userRepository = userRepository;}
    
    /**
     * Usuwa z bazy danych konta użytkowników. Konto jest usuwane jeżeli nie jest aktywne przez
     * ponad dobę licząc od daty utworzenia. Metoda jest wywoływana codziennie o godzinie 00:00.
     */
    @Scheduled(cron = "${cron.expression}")
    public void executeTask() {
        log.info("Checking database for inactive users");
        List<User> inactiveUsers = userRepository.findAll(isActive(false));
        
        for (User user : inactiveUsers) {
            if (user.getCreatedAt().isBefore(LocalDateTime.now().minusDays(1))) userRepository.delete(user);
        }
    }
}