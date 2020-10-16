package pl.lodz.p.it.auctionsystem.exceptions;

/**
 * Klasa, która reprezentuje wyjątek występujący w przypadku niepoprawnego parametru.
 */
public class InvalidParameterException extends ApplicationException {
    
    public InvalidParameterException(String message) {
        super(message);
    }
}