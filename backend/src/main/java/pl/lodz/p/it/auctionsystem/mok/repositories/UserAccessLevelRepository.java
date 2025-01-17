package pl.lodz.p.it.auctionsystem.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;

import java.util.List;
import java.util.Optional;

/**
 * Interfejs definiujący dozwolone operacje na encji {@link UserAccessLevel}.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevel, Long> {

    /**
     * Sprawdza czy w bazie danych istnieje encja {@link UserAccessLevel} na podstawie id użytkownika i
     * przypisanego mu poziomu dostępu.
     *
     * @param userId        id użytkownika
     * @param accessLevelId id poziomu dostępu
     * @return true jeśli istnieje, w przeciwnym wypadku false
     */
    boolean existsByUserIdAndAccessLevelId(Long userId, Long accessLevelId);

    /**
     * Zwraca wszystkie powiązania pomiędzy użytkownikiem o podanym id z poziomami dostępu.
     *
     * @param userId id użytkownika
     * @return lista encji typu {@link UserAccessLevel}, które wiążą użytkownika z poziomem dostępu
     */
    List<UserAccessLevel> findByUserId(Long userId);

    /**
     * Usuwa z bazy encję typu {@link UserAccessLevel} o podanym id.
     *
     * @param id id poziomu dostępu użytkownika
     */
    void deleteById(Long id);

    /**
     * Zwraca poziom dostępu użytkownika na podstawie jego id oraz id poziomu dostępu.
     *
     * @param userId        id użytkownika
     * @param accessLevelId id poziomu dostępu
     * @return obiekt encji typu {@link UserAccessLevel}, które wiążą użytkownika z poziomem dostępu
     */
    Optional<UserAccessLevel> findByUserIdAndAccessLevelId(Long userId, Long accessLevelId);
}