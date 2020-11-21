package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.utils.AccessLevelEnum;

import java.util.List;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link UserAccessLevel}
 */
@Service
public interface UserAccessLevelService {

    /**
     * Metoda przypisująca użytkownikowi poziom dostępu
     *
     * @param userId        id użytkownika, któremu ma zostać przypisany poziom dostępu
     * @param accessLevelId id poziomu dostępu do przypisania
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    void addUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException;

    /**
     * Metoda, która zwraca wszystkie poziomy dostępu użytkownika o podanym id.
     *
     * @param userId id użytkownika
     * @return lista poziomów dostępu
     */
    List<UserAccessLevel> getUserAccessLevelsByUserId(Long userId);

    /**
     * Metoda, która odbiera użytkownikowi poziom dostępu o podanym id.
     *
     * @param userAccessLevelId id poziomu dostępu
     */
    void deleteUserAccessLevel(Long userAccessLevelId);

    void deleteUserAccessLevel(Long userId, Long accessLevelId) throws ApplicationException;

    void modifyUserAccessLevels(Long userId, List<Long> accessLevelIds) throws ApplicationException;
}