package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;
import pl.lodz.p.it.auctionsystem.mok.dtos.AccessLevelDto;

import java.util.List;

/**
 * Interfejs definiujący dozwolone operacje na obiektach typu {@link AccessLevel}
 */
@Service
public interface AccessLevelService {

    /**
     * Zwraca wszystkie poziomy dostępu.
     *
     * @return lista wszystkich obiektów typu {@link AccessLevelDto}
     */
    List<AccessLevelDto> getAllAccessLevels();
}