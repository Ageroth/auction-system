package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;

import java.util.List;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link UserAccessLevel}
 */
@Service
public interface UserAccessLevelService {

    /**
     * Metoda przypisująca użytkownikowi poziom dostępu.
     *
     * @param userId        id użytkownika, któremu ma zostać przypisany poziom dostępu
     * @param accessLevelId id poziomu dostępu do przypisania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException;

    /**
     * Metoda usuwająca poziom dostępu użytkownika.
     *
     * @param userId        id użytkownika, któremu ma zostać odjęty poziom dostępu
     * @param accessLevelId id poziomu dostępu do usunięcia
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void deleteUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException;

    /**
     * Metoda modyfikująca poziomy dostępu użytkownika.
     *
     * @param userId        id użytkownika, któremego poziomy dostępu mają ulec modyfikacji
     * @param accessLevelIds id poziomów dostępu, które użytkownik ma posiadać
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void modifyUserAccessLevels(Long userId, List<Long> accessLevelIds) throws ApplicationException;
}