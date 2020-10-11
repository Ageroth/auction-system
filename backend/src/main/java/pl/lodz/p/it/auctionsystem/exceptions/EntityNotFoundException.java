package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątki dotyczące nieznalezienia obiektu encji.
 */
public class EntityNotFoundException extends ApplicationException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}