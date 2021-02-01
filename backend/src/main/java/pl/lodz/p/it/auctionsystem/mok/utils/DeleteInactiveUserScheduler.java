package pl.lodz.p.it.auctionsystem.mok.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;

/**
 * Klasa służaca do harmonogramowania cyklicznego usuwania nieaktywnych użytkowników.
 */
@Component
@RequiredArgsConstructor
public class DeleteInactiveUserScheduler {

    private static final Logger log = LoggerFactory.getLogger(DeleteInactiveUserScheduler.class);

    private final UserService userService;

    /**
     * Usuwa z bazy danych konta użytkowników. Konto jest usuwane jeżeli nie jest aktywne przez
     * ponad dobę licząc od daty utworzenia. Metoda jest wywoływana codziennie o godzinie 00:00.
     */
    @Scheduled(cron = "${cron.expression}")
    public void executeTask() {
        log.info("Checking database for inactive users");

        userService.deleteInactiveUsers();
    }
}