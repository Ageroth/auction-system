package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.entities.UserAccessLevel;

import java.util.List;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link UserAccessLevel}
 */
@Service
public interface AccessLevelService {
    
    /**
     * Zwraca wszystkie poziomy dostępu.
     *
     * @return lista wszystkich obiektów {@link AccessLevel}
     */
    List<AccessLevel> getAllAccessLevels();
}
