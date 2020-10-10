package pl.lodz.p.it.auctionsystem.mok.services;

import org.springframework.stereotype.Service;
import pl.lodz.p.it.auctionsystem.entities.AccessLevel;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public interface AccessLevelService {
    
    /**
     * Metoda która zwraca wszystkie poziomy dostępu.
     *
     * @return lista wszystkich poziomy dostępu
     */
    List<AccessLevel> getAllAccessLevels();
}
