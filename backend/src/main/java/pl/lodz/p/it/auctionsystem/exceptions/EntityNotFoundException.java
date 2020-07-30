package pl.lodz.p.it.auctionsystem.exceptions;

import lombok.Getter;

/**
 * Klasa, która reprezentuje wyjątki dotyczące nieznalezienia obiektu encji.
 */
public class EntityNotFoundException extends ApplicationException {
    
    @Getter
    private final Class<?> entityClass;
    
    public EntityNotFoundException(String errorCode, Class<?> entityClass) {
        super(errorCode);
        this.entityClass = entityClass;
    }
    
    public EntityNotFoundException(String message, String errorCode, Class<?> entityClass) {
        super(message, errorCode);
        this.entityClass = entityClass;
    }
    
    public EntityNotFoundException(Throwable cause, String errorCode, Class<?> entityClass) {
        super(cause, errorCode);
        this.entityClass = entityClass;
    }
    
    public EntityNotFoundException(String message, Throwable cause, String errorCode, Class<?> entityClass) {
        super(message, cause, errorCode);
        this.entityClass = entityClass;
    }
}

